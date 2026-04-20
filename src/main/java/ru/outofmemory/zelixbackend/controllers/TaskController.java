package ru.outofmemory.zelixbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.task.TaskRequestDto;
import ru.outofmemory.zelixbackend.dto.task.TaskResponseDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/monitors/{monitorUuid}/tasks")
    public List<TaskResponseDto> getTasksByMonitor(
            @RequestHeader("X-Api-Token") String apiToken,
            @PathVariable UUID monitorUuid
    ) {
        return taskService.getCreatedTasksByMonitor(apiToken, monitorUuid);
    }

    @GetMapping("/miners/{id}/tasks")
    public List<TaskResponseDto> getTasksByMiner(
            @AuthenticationPrincipal UserEntity userEntity,
            @PathVariable Long id
    ) {
        return taskService.getTasksByMiner(userEntity, id);
    }

    @PostMapping("/miners/tasks")
    public void createTasks(
            @AuthenticationPrincipal UserEntity userEntity,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        taskService.createTasks(userEntity, taskRequestDto);
    }

}
