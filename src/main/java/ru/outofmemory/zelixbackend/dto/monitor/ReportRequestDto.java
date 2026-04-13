package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ReportRequestDto {
    private MonitorDto monitor;
    private ArrayList<MinerDto> miners;
}
