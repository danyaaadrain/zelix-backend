package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixbackend.dto.chart.ChartDto;
import ru.outofmemory.zelixbackend.dto.chart.ChartSeries;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerMetricEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.utilities.ChartPeriod;
import ru.outofmemory.zelixbackend.utilities.ChartType;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final MetricService metricService;
    private final MinerRepo minerRepo;
    private final MinerService minerService;

    public ChartDto createMinersChart(UserEntity userEntity, MinerAlgo algo, ChartPeriod period) {
        ChartDto chartDto = new ChartDto();
        List<MinerMetricEntity> minerMetricEntities = metricService.findAllByUserAndAlgo(userEntity, period, algo);

        Map<Instant, List<MinerMetricEntity>> metricsByTime = minerMetricEntities.stream()
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
        chartDto.setTimePoints(metricsByTime.keySet());

        chartDto.getSeries().add(buildSeries(ChartType.HASHRATE, metricsByTime, MinerMetricEntity::getHashrate, algo.getUnit(), false));
        chartDto.getSeries().add(buildSeries(ChartType.POWER, metricsByTime, MinerMetricEntity::getPower, ChartType.POWER.getUnit(), false));

        return chartDto;
    }

    public ChartDto createMinerDailyChart(UserEntity userEntity, Long id) {
        ChartDto chartDto = new ChartDto();
        MinerEntity minerEntity = minerService.findMinerByUserAndId(userEntity, id);
        List<MinerHourlyMetricsEntity> minerDailyMetrics = metricService.findDailyByUserAndMinerId(userEntity, id);

        Map<Instant, List<MinerMetricEntity>> metricsByTime = minerDailyMetrics.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCreatedAt().truncatedTo(ChronoUnit.MINUTES),
                        TreeMap::new,
                        Collectors.toList()));

        chartDto.setTimePoints(metricsByTime.keySet());

        chartDto.getSeries().add(buildSeries(ChartType.HASHRATE, metricsByTime, MinerMetricEntity::getHashrate, minerEntity.getRateUnit(), true));
        chartDto.getSeries().add(buildSeries(ChartType.POWER, metricsByTime, MinerMetricEntity::getPower, ChartType.POWER.getUnit(), true));

        return chartDto;
    }

    private ChartSeries buildSeries(
            ChartType chartType,
            Map<Instant, List<MinerMetricEntity>> metricsByTime,
            ToDoubleFunction<MinerMetricEntity> function,
            String unit,
            boolean single
    ) {
        ChartSeries chartSeries = new ChartSeries();
        metricsByTime.forEach((time, metrics) ->
                chartSeries.addPoint(metrics.stream().mapToDouble(function).sum())
        );
        double min = single ? chartType.getMinSingle() : chartType.getMin();
        double max = single ? chartType.getMaxSingle() : chartType.getMax();

        chartSeries.setType(chartType);
        chartSeries.setMin(chartSeries.getPoints().stream().mapToDouble(Number::doubleValue).min().orElse(0.0) * min);
        chartSeries.setMax(chartSeries.getPoints().stream().mapToDouble(Number::doubleValue).max().orElse(0.0) * max);
        chartSeries.setLabel(chartType.getLabel());
        chartSeries.setUnit(unit);

        return chartSeries;
    }
}
