package ru.outofmemory.zelixbackend.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.entities.MinerEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.HashrateHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.entities.metrics.UserHashrateHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.repos.metrics.HashrateHourlyMetricsRepo;
import ru.outofmemory.zelixbackend.repos.metrics.UserHashrateHourlyMetricsRepo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class HashrateHourlyMetricsScheduler {
    private final MinerRepo minerRepo;
    private final HashrateHourlyMetricsRepo hashrateHourlyMetricsRepo;
    private final UserHashrateHourlyMetricsRepo userHashrateHourlyMetricsRepo;

    @Scheduled(cron = "0 */5 * * * *")
    public void schedule() {
        Instant limit = Instant.now().minus(59, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
        hashrateHourlyMetricsRepo.deleteByCreatedAtBefore(limit);
        userHashrateHourlyMetricsRepo.deleteByCreatedAtBefore(limit);

        List<MinerEntity> minerEntities = minerRepo.findAll();
        Map<UserEntity, Map<String, Double>> userHashrate = new HashMap<>();

        List<HashrateHourlyMetricsEntity> hashrateHourlyMetricsEntities = minerEntities.stream().map(minerEntity -> {
            HashrateHourlyMetricsEntity hashrateHourlyMetricsEntity = new HashrateHourlyMetricsEntity();
            hashrateHourlyMetricsEntity.setMiner(minerEntity);
            if (minerEntity.getLastReport().isAfter(Instant.now().minus(5, ChronoUnit.MINUTES))) {
                double hashrate = minerEntity.getRate();
                hashrateHourlyMetricsEntity.setHashrate(hashrate);
                userHashrate.computeIfAbsent(minerEntity.getOwner(), u -> new HashMap<>())
                        .merge(minerEntity.getAlgo(), hashrate, Double::sum);
            }
            return hashrateHourlyMetricsEntity;
        }).toList();


        List<UserHashrateHourlyMetricsEntity> userHashrateHourlyMetricsEntities = new ArrayList<>();

        userHashrate.forEach((userEntity, hashratePerAlgo) -> {
            hashratePerAlgo.forEach((algo, hashrate) -> {
                UserHashrateHourlyMetricsEntity userHashrateHourlyMetricsEntity = new UserHashrateHourlyMetricsEntity();

                userHashrateHourlyMetricsEntity.setUser(userEntity);
                userHashrateHourlyMetricsEntity.setAlgo(algo);
                userHashrateHourlyMetricsEntity.setHashrate(hashrate);

                userHashrateHourlyMetricsEntities.add(userHashrateHourlyMetricsEntity);
            });
        });

        userHashrateHourlyMetricsRepo.saveAll(userHashrateHourlyMetricsEntities);
        hashrateHourlyMetricsRepo.saveAll(hashrateHourlyMetricsEntities);
    }
}
