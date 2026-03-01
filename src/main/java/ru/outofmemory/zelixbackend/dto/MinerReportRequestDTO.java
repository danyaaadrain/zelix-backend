package ru.outofmemory.zelixbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.dto.miner.MinerDTO;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MinerReportRequestDTO {
    @JsonProperty("api_key")
    private String apiKey;
    private ArrayList<MinerDTO> miners;
}
