package ru.outofmemory.zelixbackend.dto.status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusResponseDTO {
    private int totalMiners;
    private int onlineMiners;
    private int offlineMiners;

    private int totalPowerConsumption;

    private double btcAvgHashrate;
    private double ltcAvgHashrate;
}
