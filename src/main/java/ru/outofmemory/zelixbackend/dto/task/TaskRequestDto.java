package ru.outofmemory.zelixbackend.dto.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.util.List;

@Data
@NoArgsConstructor
public class TaskRequestDto {
    private List<Long> minerIds;
    private MinerTask task;
    private Long payload;
}
