package ru.outofmemory.zelixbackend.dto.miner;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MinerDto {
    private Long id;
    private String name;

    @NotNull
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

    @NotNull
    @Valid
    private Set<ChainDto> chains = new HashSet<>();

    @NotNull
    @Valid
    private Set<PoolDto> pools = new HashSet<>();

    private Boolean online;
}
