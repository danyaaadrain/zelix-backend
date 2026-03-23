package ru.outofmemory.zelixbackend.dto.miner;

import lombok.Data;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.util.List;
import java.util.UUID;

@Data
public class MinerCardDto {
    private Long id;
    private UUID uuid;
    private String ip;
    private String mac;
    private String type;
    private String sn;
    private MinerAlgo algo;
    private Integer power;
    private Double rate;
    private Double rateAvg;
    private String rateUnit;
    private List<Integer> fans;
    private List<Integer> chipTemps;

    private Boolean online;
}
