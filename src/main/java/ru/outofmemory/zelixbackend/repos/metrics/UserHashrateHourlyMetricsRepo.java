package ru.outofmemory.zelixbackend.repos.metrics;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.UserHashrateHourlyMetricsEntity;

import java.time.Instant;
import java.util.List;

public interface UserHashrateHourlyMetricsRepo extends JpaRepository<UserHashrateHourlyMetricsEntity, Long> {
    void deleteByCreatedAtBefore(Instant instant);

    List<UserHashrateHourlyMetricsEntity> findAllByUserIdAndAlgo(Long id, String algo);
}
