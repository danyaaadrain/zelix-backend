package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.dto.monitor.MonitorReportDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.MinerService;
import ru.outofmemory.zelixbackend.services.MonitorService;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final MinerService minerService;
    private final MonitorService monitorService;

    @PostMapping("/report")
    public ResponseEntity<?> report(@RequestBody MonitorReportDto reportRequest) {
        UserEntity userEntity = monitorService.findUserByApiKey(reportRequest.getApiToken());
        MonitorEntity monitorEntity = monitorService.getMonitorByUuid(reportRequest.getMonitorUuid());

        monitorService.saveMonitor(monitorEntity, userEntity, reportRequest);
        minerService.saveMiners(reportRequest.getMiners(), userEntity, monitorEntity);

        return ResponseEntity.ok().build();
    }
}
