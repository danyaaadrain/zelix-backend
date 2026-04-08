package ru.outofmemory.zelixbackend.dto.chart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ChartDto {
    Set<Instant> timePoints;
    private Double averageHashrate;
    private Double currentHashrate;
    private String unit;
    private List<ChartSeries> series = new ArrayList<>();
}
