package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.miner.MinerTaskEntity;

import java.util.List;
import java.util.UUID;

public interface MinerTaskRepo extends JpaRepository<MinerTaskEntity, Long> {
    List<MinerTaskEntity> findAllByMonitorId(UUID monitorId);
}