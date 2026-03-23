package ru.outofmemory.zelixbackend.repos.metrics;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.metrics.MinerHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.time.Instant;
import java.util.List;

public interface MinerHourlyMetricsRepo extends JpaRepository<MinerHourlyMetricsEntity, Long> {
    void deleteByCreatedAtBefore(Instant instant);

    List<MinerHourlyMetricsEntity> findAllByOwnerIdAndAlgo(Long id, MinerAlgo algo);

    List<MinerHourlyMetricsEntity> findAllByOwnerId(Long id);
}

