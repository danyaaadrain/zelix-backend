package ru.outofmemory.zelixbackend.dto.miner;

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

    public double rate;
    public double rate_avg;
    public String rate_unit;
    public int[] fans = new int[4];
    public int power;
    public long uptime;

    public ArrayList<ChainDTO> chains = new ArrayList<>();
    public ArrayList<PoolDTO> pools = new ArrayList<>();
}