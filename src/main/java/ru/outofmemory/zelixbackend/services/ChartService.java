package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixbackend.dto.charts.ChartPoint;
import ru.outofmemory.zelixbackend.dto.charts.SummaryChartDTO;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.UserHashrateHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.repos.metrics.UserHashrateHourlyMetricsRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final UserHashrateHourlyMetricsRepo userHashrateHourlyMetricsRepo;

    private static final Map<String, String> ALGO_UNITS = Map.of(
            "BTC", "TH/s",
            "LTC", "GH/s"
    );


    public SummaryChartDTO getSummaryChart(UserEntity userEntity, String algo) {
        List<UserHashrateHourlyMetricsEntity> userHashrateHourlyMetricsEntities =
                userHashrateHourlyMetricsRepo.findAllByUserIdAndAlgo(userEntity.getId(), algo);
        SummaryChartDTO summaryChartDto = new SummaryChartDTO();
        summaryChartDto.setAlgo(algo);

        double averageHashrate = userHashrateHourlyMetricsEntities.stream()
                .mapToDouble(UserHashrateHourlyMetricsEntity::getHashrate)
                .average()
                .orElse(0.0);

        List<ChartPoint> chartPoints = userHashrateHourlyMetricsEntities.stream().map(userHashrateHourlyMetricsEntity ->  {
            ChartPoint chartPoint = new ChartPoint();
            chartPoint.setTime(userHashrateHourlyMetricsEntity.getCreatedAt());
            chartPoint.setHashrate(userHashrateHourlyMetricsEntity.getHashrate());
            return chartPoint;
        }).toList();

        summaryChartDto.setAverageHashrate(BigDecimal.valueOf(averageHashrate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        summaryChartDto.setUnit(ALGO_UNITS.get(algo));
        summaryChartDto.setPoints(chartPoints);

        return summaryChartDto;
    }
}
