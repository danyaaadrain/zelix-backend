package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.task.TaskResponseDto;
import ru.outofmemory.zelixbackend.services.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/monitors")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{monitorUuid}/tasks")
    public List<TaskResponseDto> getTasks(
            @RequestHeader("X-Api-Token") String apiToken,
            @PathVariable UUID monitorUuid
    ) {
        return taskService.getTasks(apiToken, monitorUuid);
    }
}
