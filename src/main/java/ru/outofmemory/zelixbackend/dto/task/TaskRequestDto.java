package ru.outofmemory.zelixbackend.dto.task;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskRequestDto {
    private String apiToken;
    private UUID monitorUuid;
}
