package ru.outofmemory.zelixbackend.dto.miner;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChainDTO {
    public int id;
    public int chip_count;
    public int[] chip_temp;
    public String chip_status;
    public int pcb_min;
    public int pcb_max;
    public long hw_errors;
}
