package ru.outofmemory.zelixbackend.dto.monitor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;
import ru.outofmemory.zelixbackend.dto.task.TaskStatusDto;

import java.util.List;

@Data
@NoArgsConstructor
public class ReportRequestDto {
    @NotNull
    @Valid
    private MonitorDto monitor;

    @NotNull
    @Valid
    private List<MinerDto> miners;

    private List<TaskStatusDto> tasks;
}
