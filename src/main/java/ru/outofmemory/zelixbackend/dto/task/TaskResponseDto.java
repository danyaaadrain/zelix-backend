package ru.outofmemory.zelixbackend.dto.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private UUID minerUuid;
    private MinerTask task;
    private String payload;
}
