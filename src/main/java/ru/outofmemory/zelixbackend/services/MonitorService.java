package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.monitor.MonitorReportDTO;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.repos.MonitorRepo;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MonitorService {
    private final MonitorRepo monitorRepo;

    public MonitorEntity getMonitorByUUID(String uuid) {
        UUID monitorUuid = UUID.fromString(uuid);
        MonitorEntity monitorEntity = monitorRepo.findById(monitorUuid).orElseGet(MonitorEntity::new);
        monitorEntity.setId(monitorUuid);
        return monitorEntity;
    }

    public void saveMonitor(MonitorEntity monitorEntity, UserEntity owner, MonitorReportDTO monitorReportRequestDTO) {
        monitorEntity.setMonitorIp(monitorReportRequestDTO.getMonitorIp());
        monitorEntity.setMonitorMac(monitorReportRequestDTO.getMonitorMac());
        monitorEntity.setUptime(monitorReportRequestDTO.getUptimeMillis());
        monitorEntity.setOsName(monitorReportRequestDTO.getOsName());
        monitorEntity.setOwner(owner);

        monitorRepo.save(monitorEntity);
    }
}
