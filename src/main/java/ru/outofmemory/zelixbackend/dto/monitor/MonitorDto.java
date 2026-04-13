package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonitorDto {
    private String monitorIp;
    private String monitorMac;
    private Long uptimeMillis;
    private String osName;
}
