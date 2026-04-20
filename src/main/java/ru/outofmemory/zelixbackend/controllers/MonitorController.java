package ru.outofmemory.zelixbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.monitor.ReportRequestDto;
import ru.outofmemory.zelixbackend.services.MinerService;
import ru.outofmemory.zelixbackend.services.MonitorService;
import ru.outofmemory.zelixbackend.services.TaskService;
import ru.outofmemory.zelixbackend.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/monitors")
@RequiredArgsConstructor
public class MonitorController {
    private final MinerService minerService;
    private final MonitorService monitorService;
    private final UserService userService;
    private final TaskService taskService;

    @PostMapping("/{monitorUuid}/reports")
    public void report(
            @RequestHeader("X-Api-Token") String apiToken,
            @Valid @RequestBody ReportRequestDto reportRequest,
            @PathVariable UUID monitorUuid
    ) {
        var userEntity = userService.findUserByApiKey(apiToken);
        var monitorEntity = monitorService.getMonitorByUuidAndOwnerId(monitorUuid, userEntity.getId());

        monitorService.saveMonitor(userEntity, monitorEntity, reportRequest.getMonitor());
        minerService.saveMiners(userEntity, monitorEntity, reportRequest.getMiners());

        taskService.deleteCompletedTasks(monitorEntity, reportRequest.getCompletedTasks());
    }
}
