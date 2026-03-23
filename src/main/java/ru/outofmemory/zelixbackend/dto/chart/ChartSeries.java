package ru.outofmemory.zelixbackend.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartSeries {
    List<Double> points = new ArrayList<>();
    private Double min;
    private Double max;
    private String label;
    private String unit;

    public void addPoint(Double point) {
        points.add(point);
    }
}
