package ru.outofmemory.zelixbackend.repos.metrics;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.metrics.MinerDailyMetricsEntity;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.time.Instant;
import java.util.List;

public interface MinerDailyMetricsRepo extends JpaRepository<MinerDailyMetricsEntity, Long> {
    void deleteByCreatedAtBefore(Instant instant);

    List<MinerDailyMetricsEntity> findAllByOwnerIdAndAlgo(Long id, MinerAlgo algo);

    List<MinerDailyMetricsEntity> findAllByOwnerId(Long id);
}

