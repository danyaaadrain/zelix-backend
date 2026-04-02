package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.monitor.MonitorReportDto;
import ru.outofmemory.zelixbackend.services.MinerService;
import ru.outofmemory.zelixbackend.services.MonitorService;
import ru.outofmemory.zelixbackend.services.UserService;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final MinerService minerService;
    private final MonitorService monitorService;
    private final UserService userService;

    @PostMapping("/report")
    public void report(@RequestHeader("X-Api-Token") String apiToken, @RequestBody MonitorReportDto reportRequest) {
        var userEntity = userService.findUserByApiKey(apiToken);
        var monitorEntity = monitorService.getMonitorByUuidAndOwnerId(reportRequest.getMonitorUuid(), userEntity.getId());

        monitorService.saveMonitor(monitorEntity, userEntity, reportRequest);
        minerService.saveMiners(reportRequest.getMiners(), userEntity, monitorEntity);
    }
}
