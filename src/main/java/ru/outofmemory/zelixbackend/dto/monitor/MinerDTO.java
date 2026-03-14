package ru.outofmemory.zelixbackend.dto.monitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MinerDTO {
    public String id;
    public String ip;
    public String mac;
    public String type;
    public String sn;
    public String algo;

    public double rate;
    @JsonProperty("rate_avg")
    public double rateAvg;
    @JsonProperty("rate_unit")
    public String rateUnit;
    public int[] fans = new int[4];
    public int power;
    public long uptime;

    public ArrayList<ChainDTO> chains = new ArrayList<>();
    public ArrayList<PoolDTO> pools = new ArrayList<>();
}