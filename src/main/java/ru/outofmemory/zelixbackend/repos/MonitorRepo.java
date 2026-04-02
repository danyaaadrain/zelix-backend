package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;

import java.util.Optional;
import java.util.UUID;

public interface MonitorRepo extends JpaRepository<MonitorEntity, UUID> {
    Optional<MonitorEntity> findByIdAndOwnerId(UUID id, Long ownerId);
}
