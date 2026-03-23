package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.monitor.MonitorReportDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.repos.MonitorRepo;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MonitorService {
    private final MonitorRepo monitorRepo;

    public MonitorEntity getMonitorByUuid(UUID uuid) {
        MonitorEntity monitorEntity = monitorRepo.findById(uuid).orElseGet(MonitorEntity::new);
        monitorEntity.setId(uuid);
        return monitorEntity;
    }

    public void saveMonitor(MonitorEntity monitorEntity, UserEntity owner, MonitorReportDto monitorReportRequestDTO) {
        monitorEntity.setMonitorIp(monitorReportRequestDTO.getMonitorIp());
        monitorEntity.setMonitorMac(monitorReportRequestDTO.getMonitorMac());
        monitorEntity.setUptime(monitorReportRequestDTO.getUptimeMillis());
        monitorEntity.setOsName(monitorReportRequestDTO.getOsName());
        monitorEntity.setOwner(owner);

        monitorRepo.save(monitorEntity);
    }
}
