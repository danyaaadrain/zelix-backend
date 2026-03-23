package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MinerDto {
    private UUID uuid;
    private String ip;
    private String mac;
    private String type;
    private String sn;
    private MinerAlgo algo;

    private double rate;
    private double rateAvg;
    private String rateUnit;
    private List<Integer> fans;
    private int power;
    private long uptime;

    private List<ChainDto> chains = new ArrayList<>();
    private List<PoolDto> pools = new ArrayList<>();
}