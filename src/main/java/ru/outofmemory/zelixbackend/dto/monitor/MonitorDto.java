package ru.outofmemory.zelixbackend.dto.monitor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonitorDto {
    @NotBlank
    private String monitorIp;

    @NotBlank
    private String monitorMac;

    @NotNull
    @PositiveOrZero
    private Long uptimeMillis;

    @NotBlank
    private String osName;
}
