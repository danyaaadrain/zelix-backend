package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MonitorReportDto {
    private UUID monitorUuid;
    private String monitorIp;
    private String monitorMac;
    private Long uptimeMillis;
    private String osName;
    private ArrayList<MinerDto> miners;
}
