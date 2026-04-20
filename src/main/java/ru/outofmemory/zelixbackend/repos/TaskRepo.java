package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.miner.TaskEntity;
import ru.outofmemory.zelixbackend.utilities.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskRepo extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByMonitorIdAndStatus(UUID monitorId, TaskStatus status);
    List<TaskEntity> findAllByMinerId(Long minerId);
    List<TaskEntity> findAllByMonitorId(UUID monitorId);
}