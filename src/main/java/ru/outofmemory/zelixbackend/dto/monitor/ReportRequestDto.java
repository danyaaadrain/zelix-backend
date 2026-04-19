package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;

import java.util.List;

@Data
@NoArgsConstructor
public class ReportRequestDto {
    private MonitorDto monitor;
    private List<MinerDto> miners;
    private List<Long> completedTasks;
}
