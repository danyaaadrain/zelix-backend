package ru.outofmemory.zelixbackend.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.utilities.ChartType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartSeries {
    private ChartType type;
    private List<Double> points = new ArrayList<>();
    private Double min;
    private Double max;
    private String label;
    private String unit;

    public void addPoint(Double point) {
        points.add(point);
    }
}
