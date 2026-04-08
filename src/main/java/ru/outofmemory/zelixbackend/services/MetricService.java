package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.status.StatusResponseDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.BaseMinerMetricEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.repos.metrics.MinerDailyMetricsRepo;
import ru.outofmemory.zelixbackend.repos.metrics.MinerHourlyMetricsRepo;
import ru.outofmemory.zelixbackend.utilities.ChartPeriod;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MetricService {
    private final MinerHourlyMetricsRepo minerHourlyMetricsRepo;
    private final MinerDailyMetricsRepo minerDailyMetricsRepo;

    private final MinerRepo minerRepo;

    public List<BaseMinerMetricEntity> getMetricsByUser(UserEntity userEntity, ChartPeriod period) {
        return switch (period) {
            case HOURLY -> new ArrayList<>(minerHourlyMetricsRepo.findAllByOwnerId(userEntity.getId()));
            case DAILY -> new ArrayList<>(minerDailyMetricsRepo.findAllByOwnerId(userEntity.getId()));
        };
    }

    public List<BaseMinerMetricEntity> findAllByUserAndAlgo(UserEntity userEntity, ChartPeriod period, MinerAlgo algo) {
        return switch (period) {
            case HOURLY -> new ArrayList<>(minerHourlyMetricsRepo.findAllByOwnerIdAndAlgo(userEntity.getId(), algo));
            case DAILY -> new ArrayList<>(minerDailyMetricsRepo.findAllByOwnerIdAndAlgo(userEntity.getId(), algo));
        };
    }

    public double countAverageHashrate(Map<Instant, List<BaseMinerMetricEntity>> metricsByTime) {
        double value = metricsByTime.values().stream()
                .mapToDouble(list ->
                        list.stream()
                                .mapToDouble(BaseMinerMetricEntity::getHashrate)
                                .sum()
                )
                .average()
                .orElse(0.0);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public StatusResponseDto getStatus(UserEntity userEntity) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        Instant time = Instant.now().minus(1, ChronoUnit.MINUTES);
        statusResponseDto.setTotalMiners(minerRepo.countAllByOwnerId(userEntity.getId()));
        statusResponseDto.setOnlineMiners(minerRepo.countAllByOwnerIdAndLastReportIsAfter(userEntity.getId(), time));
        statusResponseDto.setOfflineMiners(statusResponseDto.getTotalMiners() - statusResponseDto.getOnlineMiners());
        statusResponseDto.setTotalPowerConsumption(minerRepo.sumPowerByOwnerIdAndLastReportIsAfter(userEntity.getId(), time));
        return statusResponseDto;
    }
}
