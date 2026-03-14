package ru.outofmemory.zelixbackend.dto.monitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MonitorReportDTO {
    @JsonProperty("monitor_uuid")
    private String monitorUuid;
    @JsonProperty("api_key")
    private String apiKey;
    @JsonProperty("monitor_ip")
    public String monitorIp;
    @JsonProperty("monitor_mac")
    public String monitorMac;
    @JsonProperty("uptime")
    public Long uptimeMillis;
    @JsonProperty("os_name")
    public String osName;
    @JsonProperty("miners")
    private ArrayList<MinerDTO> miners;
}
