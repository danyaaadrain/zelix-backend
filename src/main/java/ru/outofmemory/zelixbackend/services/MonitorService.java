package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.monitor.MonitorDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.repos.MonitorRepo;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MonitorService {
    private final MonitorRepo monitorRepo;

    public MonitorEntity getMonitorByUuidAndOwnerId(UUID uuid, Long id) {
        MonitorEntity monitorEntity = monitorRepo.findByIdAndOwnerId(uuid, id).orElseGet(MonitorEntity::new);
        monitorEntity.setId(uuid);
        return monitorEntity;
    }

    public void saveMonitor(UserEntity owner, MonitorEntity monitorEntity, MonitorDto monitorDto) {
        monitorEntity.setMonitorIp(monitorDto.getMonitorIp());
        monitorEntity.setMonitorMac(monitorDto.getMonitorMac());
        monitorEntity.setUptime(monitorDto.getUptimeMillis());
        monitorEntity.setOsName(monitorDto.getOsName());
        monitorEntity.setOwner(owner);

        monitorRepo.save(monitorEntity);
    }
}
