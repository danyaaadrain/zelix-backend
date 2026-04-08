package ru.outofmemory.zelixbackend.dto.miner;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoolDto {
    public int poolId;
    public int priority;
    public String url;
    public String username;
    public String password;
    public String status;
    public long accepted;
    public long rejected;
    public long stale;
    public String lastShareTime;
    public double diff;
}
