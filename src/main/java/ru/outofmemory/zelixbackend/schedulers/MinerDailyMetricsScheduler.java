package ru.outofmemory.zelixbackend.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.entities.MinerEntity;
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
            MinerDailyMetricsEntity minerDailyMetricsEntity = new MinerDailyMetricsEntity();
            minerDailyMetricsEntity.setMiner(minerEntity);
            minerDailyMetricsEntity.setOwner(minerEntity.getOwner());
            minerDailyMetricsEntity.setAlgo(minerEntity.getAlgo());
            minerDailyMetricsEntity.setPower(minerEntity.getPower());

            double avg = metrics.stream()
                    .mapToDouble(MinerHourlyMetricsEntity::getHashrate)
                    .average()
                    .orElse(0.0);

            minerDailyMetricsEntity.setHashrate(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP).doubleValue());
            hashrateDailyMetricsEntities.add(minerDailyMetricsEntity);
        });

        minerDailyMetricsRepo.saveAll(hashrateDailyMetricsEntities);
    }
}
