package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.monitor.MonitorReportDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.repos.UserRepo;
import ru.outofmemory.zelixbackend.services.MinerService;
import ru.outofmemory.zelixbackend.services.MonitorService;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final MinerService minerService;
    private final MonitorService monitorService;
    private final UserRepo userRepo;

    @PostMapping("/report")
    public ResponseEntity<?> report(@RequestBody MonitorReportDto reportRequest) {
        UserEntity userEntity = userRepo.findByApiKeyIgnoreCase(reportRequest.getApiToken()).orElseThrow(() ->
                new RuntimeException("Invalid api key")
        );

        MonitorEntity monitorEntity = monitorService.getMonitorByUuid(reportRequest.getMonitorUuid());
        monitorService.saveMonitor(monitorEntity, userEntity, reportRequest);
        minerService.saveMiners(reportRequest.getMiners(), userEntity, monitorEntity);

        return ResponseEntity.ok().build();
    }
}
