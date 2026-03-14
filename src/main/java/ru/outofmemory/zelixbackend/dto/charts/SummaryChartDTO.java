package ru.outofmemory.zelixbackend.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryChartDTO {
    private String algo;
    private Double averageHashrate;
    private String unit;
    private List<ChartPoint> points;
}
