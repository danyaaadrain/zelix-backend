package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.miner.TaskEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TaskRepo extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByMonitorId(UUID monitorId);
    List<TaskEntity> findAllByMinerId(Long minerId);

    void deleteAllByMonitorIdAndIdIn(UUID monitorId, Collection<Long> ids);
}