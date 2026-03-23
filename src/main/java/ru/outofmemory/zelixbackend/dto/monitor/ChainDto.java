package ru.outofmemory.zelixbackend.dto.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChainDto {
    private Integer chainId;
    private Integer chipCount;
    private List<Integer> chipTemp;
    private String chipStatus;
    private Integer pcbMin;
    private Integer pcbMax;
    private Long hwErrors;
}
