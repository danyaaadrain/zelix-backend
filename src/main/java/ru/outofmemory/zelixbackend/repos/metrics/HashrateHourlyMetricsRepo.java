package ru.outofmemory.zelixbackend.repos.metrics;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.metrics.HashrateHourlyMetricsEntity;

import java.time.Instant;

public interface HashrateHourlyMetricsRepo extends JpaRepository<HashrateHourlyMetricsEntity, Long> {
    void deleteByCreatedAtBefore(Instant instant);
}
