package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixbackend.dto.chart.ChartDto;
import ru.outofmemory.zelixbackend.dto.chart.ChartSeries;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.BaseMinerMetricEntity;
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

        //chartDto.getSeries().add(buildSeries(ChartType.HASHRATE, metricsByTime, BaseMinerMetricEntity::getHashrate, algo.getUnit()));
        //chartDto.getSeries().add(buildSeries(ChartType.POWER, metricsByTime, BaseMinerMetricEntity::getPower, ChartType.POWER.getUnit()));


        chartDto.getSeries().add(buildSeries(
                ChartType.HASHRATE,
                metricsByTime,
                BaseMinerMetricEntity::getHashrate,
                algo.getUnit(),
                ChartType.HASHRATE.getMin(),
                ChartType.HASHRATE.getMax()
        ));
        chartDto.getSeries().add(buildSeries(
                ChartType.POWER,
                metricsByTime,
                BaseMinerMetricEntity::getPower,
                ChartType.POWER.getUnit(),
                ChartType.POWER.getMin(),
                ChartType.POWER.getMax()
        ));

        return chartDto;
    }

    public ChartDto createMinerDailyChart(UserEntity userEntity, Long id) {
        ChartDto chartDto = new ChartDto();
        MinerEntity minerEntity = minerService.findMinerByUserAndId(userEntity, id);
        List<MinerHourlyMetricsEntity> minerDailyMetrics = metricService.findDailyByUserAndMinerId(userEntity, id);

        Map<Instant, List<BaseMinerMetricEntity>> metricsByTime = minerDailyMetrics.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCreatedAt().truncatedTo(ChronoUnit.MINUTES),
                        TreeMap::new,
                        Collectors.toList()));

        chartDto.setUnit(minerEntity.getRateUnit());
        chartDto.setTimePoints(metricsByTime.keySet());

        chartDto.getSeries().add(buildSeries(
                ChartType.HASHRATE,
                metricsByTime,
                BaseMinerMetricEntity::getHashrate,
                minerEntity.getRateUnit(),
                ChartType.HASHRATE.getMinSolo(),
                ChartType.HASHRATE.getMaxSolo()
        ));
        chartDto.getSeries().add(buildSeries(
                ChartType.POWER,
                metricsByTime,
                BaseMinerMetricEntity::getPower,
                ChartType.POWER.getUnit(),
                ChartType.POWER.getMinSolo(),
                ChartType.POWER.getMaxSolo()
        ));

        return chartDto;
    }

    private ChartSeries buildSeries(
            ChartType chartType,
            Map<Instant, List<BaseMinerMetricEntity>> metricsByTime,
            ToDoubleFunction<BaseMinerMetricEntity> function,
            String unit,
            double min,
            double max
    ) {
        ChartSeries chartSeries = new ChartSeries();
        metricsByTime.forEach((time, metrics) ->
                chartSeries.addPoint(metrics.stream().mapToDouble(function).sum())
        );
        chartSeries.setType(chartType);
        chartSeries.setMin(chartSeries.getPoints().stream().mapToDouble(Number::doubleValue).min().orElse(0.0) * min);
        chartSeries.setMax(chartSeries.getPoints().stream().mapToDouble(Number::doubleValue).max().orElse(0.0) * max);
        chartSeries.setLabel(chartType.getLabel());
        chartSeries.setUnit(unit);

        return chartSeries;
    }
}
