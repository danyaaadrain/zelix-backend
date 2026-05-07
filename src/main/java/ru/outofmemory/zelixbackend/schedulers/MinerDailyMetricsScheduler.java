package ru.outofmemory.zelixbackend.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerDailyMetricsEntity;
import ru.outofmemory.zelixbackend.repos.metrics.MinerDailyMetricsRepo;
import ru.outofmemory.zelixbackend.repos.metrics.MinerHourlyMetricsRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MinerDailyMetricsScheduler {
    private final MinerHourlyMetricsRepo minerHourlyMetricsRepo;
    private final MinerDailyMetricsRepo minerDailyMetricsRepo;

    @Scheduled(cron = "0 0 * * * *")
    public void schedule() {
        Instant limit = Instant.now().minus(23, ChronoUnit.HOURS).truncatedTo(ChronoUnit.HOURS);
        minerDailyMetricsRepo.deleteByCreatedAtBefore(limit);

        List<MinerHourlyMetricsEntity> hashrateHourlyMetricsEntities = minerHourlyMetricsRepo.findAll();
        List<MinerDailyMetricsEntity> hashrateDailyMetricsEntities = new ArrayList<>();

        Map<MinerEntity, List<MinerHourlyMetricsEntity>> byMiner =
                hashrateHourlyMetricsEntities.stream().collect(Collectors.groupingBy(MinerHourlyMetricsEntity::getMiner));

        byMiner.forEach((minerEntity, metrics) -> {
            MinerDailyMetricsEntity minerMetricsEntity = new MinerDailyMetricsEntity();
            minerMetricsEntity.setMiner(minerEntity);
            minerMetricsEntity.setOwner(minerEntity.getOwner());
            minerMetricsEntity.setAlgo(minerEntity.getAlgo());
            minerMetricsEntity.setPower(minerEntity.getPower());
            minerMetricsEntity.setCreatedAt(Instant.now());


            double powerAvg = metrics.stream()
                    .mapToInt(MinerHourlyMetricsEntity::getPower)
                    .average()
                    .orElse(0);

            double rateAvg = metrics.stream()
                    .mapToDouble(MinerHourlyMetricsEntity::getHashrate)
                    .average()
                    .orElse(0);

            minerMetricsEntity.setHashrate(BigDecimal.valueOf(rateAvg).setScale(2, RoundingMode.HALF_UP).doubleValue());
            minerMetricsEntity.setPower(BigDecimal.valueOf(powerAvg).setScale(0, RoundingMode.DOWN).intValue());
            hashrateDailyMetricsEntities.add(minerMetricsEntity);
        });

        minerDailyMetricsRepo.saveAll(hashrateDailyMetricsEntities);
    }
}
