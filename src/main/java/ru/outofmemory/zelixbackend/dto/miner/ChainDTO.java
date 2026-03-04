package ru.outofmemory.zelixbackend.dto.miner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChainDTO {
    public int id;

    @JsonProperty("chip_count")
    public int chipCount;
    @JsonProperty("chip_temp")
    public int[] chipTemp;
    @JsonProperty("chip_status")
    public String chipStatus;
    @JsonProperty("pcb_min")
    public int pcbMin;
    @JsonProperty("pcb_max")
    public int pcbMax;
    @JsonProperty("hw_errors")
    public long hwErrors;
}
