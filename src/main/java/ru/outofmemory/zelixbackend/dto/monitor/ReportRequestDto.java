package ru.outofmemory.zelixbackend.dto.monitor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;

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

    private List<@Positive Long> completedTasks;
}
