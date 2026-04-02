package ru.outofmemory.zelixbackend.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.repos.metrics.MinerHourlyMetricsRepo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MinerHourlyMetricsScheduler {
    private final MinerRepo minerRepo;
    private final MinerHourlyMetricsRepo minerHourlyMetricsRepo;

    @Scheduled(cron = "0 */5 * * * *")
    public void schedule() {
        Instant limit = Instant.now().minus(59, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
        minerHourlyMetricsRepo.deleteByCreatedAtBefore(limit);

        List<MinerEntity> minerEntities = minerRepo.findAll();

        List<MinerHourlyMetricsEntity> hashrateHourlyMetricsEntities = minerEntities.stream()
                .map(minerEntity -> {
                    MinerHourlyMetricsEntity minerHourlyMetricsEntity = new MinerHourlyMetricsEntity();
                    if (!minerEntity.getLastReport().isAfter(Instant.now().minus(1, ChronoUnit.MINUTES))) {
                        minerHourlyMetricsEntity.setMiner(minerEntity);
                        minerHourlyMetricsEntity.setOwner(minerEntity.getOwner());
                        minerHourlyMetricsEntity.setAlgo(minerEntity.getAlgo());
                        minerHourlyMetricsEntity.setPower(0);
                        minerHourlyMetricsEntity.setHashrate(0);

                        return minerHourlyMetricsEntity;
                    }
                    minerHourlyMetricsEntity.setMiner(minerEntity);
                    minerHourlyMetricsEntity.setOwner(minerEntity.getOwner());
                    minerHourlyMetricsEntity.setAlgo(minerEntity.getAlgo());
                    minerHourlyMetricsEntity.setPower(minerEntity.getPower());
                    minerHourlyMetricsEntity.setHashrate(minerEntity.getRate());

                    return minerHourlyMetricsEntity;
                }).toList();

        minerHourlyMetricsRepo.saveAll(hashrateHourlyMetricsEntities);
    }
}
