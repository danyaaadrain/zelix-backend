package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MonitorReportDto {
    private String monitorIp;
    private String monitorMac;
    private Long uptimeMillis;
    private String osName;
    private ArrayList<MinerDto> miners;
}
