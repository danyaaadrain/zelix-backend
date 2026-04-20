package ru.outofmemory.zelixbackend.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.outofmemory.zelixbackend.utilities.TaskStatus;

@Data
@AllArgsConstructor
public class TaskStatusDto {
    private Long id;
    private TaskStatus status;
}
