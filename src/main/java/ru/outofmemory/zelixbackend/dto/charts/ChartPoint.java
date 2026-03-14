package ru.outofmemory.zelixbackend.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartPoint {
    private Instant time;
    private Double hashrate;
}
