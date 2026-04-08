package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixbackend.dto.chart.ChartDto;
import ru.outofmemory.zelixbackend.dto.chart.ChartSeries;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.BaseMinerMetricEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.utilities.ChartPeriod;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final MetricService metricService;
    private final MinerRepo minerRepo;

    public ChartDto createMinersChart(UserEntity userEntity, MinerAlgo algo, ChartPeriod period) {
        ChartDto chartDto = new ChartDto();
        List<BaseMinerMetricEntity> minerMetricEntities = metricService.findAllByUserAndAlgo(userEntity, period, algo);

        Map<Instant, List<BaseMinerMetricEntity>> metricsByTime = minerMetricEntities.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCreatedAt().truncatedTo(ChronoUnit.MINUTES),
                        TreeMap::new,
                        Collectors.toList()));
        chartDto.setAverageHashrate(metricService.countAverageHashrate(metricsByTime));
        Double currentHashrate = minerRepo.sumRateByOwnerIdAndAlgoAndLastReportIsAfter(
                userEntity.getId(),
                algo,
                Instant.now().minus(1, ChronoUnit.MINUTES));
        chartDto.setCurrentHashrate(Objects.requireNonNullElse(currentHashrate, 0.0));
        chartDto.setUnit(algo.getUnit());
        chartDto.setTimePoints(metricsByTime.keySet());

        ChartSeries hashrateSeries = new ChartSeries();
        ChartSeries powerSeries = new ChartSeries();

        metricsByTime.forEach((time, metrics) -> {
            hashrateSeries.addPoint(metrics.stream().mapToDouble(BaseMinerMetricEntity::getHashrate).sum());
            powerSeries.addPoint(metrics.stream().mapToDouble(BaseMinerMetricEntity::getPower).sum());
        });

        hashrateSeries.setMin(hashrateSeries.getPoints().stream().mapToDouble(Number::doubleValue).min().orElse(0.0) * 0.9);
        hashrateSeries.setMax(hashrateSeries.getPoints().stream().mapToDouble(Number::doubleValue).max().orElse(0.0) * 1.05);
        hashrateSeries.setLabel("Хешрейт");
        hashrateSeries.setUnit(algo.getUnit());

        powerSeries.setMin(powerSeries.getPoints().stream().mapToDouble(Number::doubleValue).min().orElse(0.0) * 0.2);
        powerSeries.setMax(powerSeries.getPoints().stream().mapToDouble(Number::doubleValue).max().orElse(0.0) * 8);
        powerSeries.setLabel("Потребление");
        powerSeries.setUnit("Вт");

        chartDto.getSeries().add(hashrateSeries);
        chartDto.getSeries().add(powerSeries);

        return chartDto;
    }
}
