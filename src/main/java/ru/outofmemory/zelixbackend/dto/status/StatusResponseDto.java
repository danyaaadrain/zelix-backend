package ru.outofmemory.zelixbackend.dto.status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusResponseDto {
    private Integer totalMiners;
    private Integer onlineMiners;
    private Integer offlineMiners;
    private Double totalPowerConsumption;
}
