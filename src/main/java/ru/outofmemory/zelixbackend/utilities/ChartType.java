package ru.outofmemory.zelixbackend.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChartType {
    HASHRATE("Хешрейт", null, 0.9, 1.05, 0.9, 1.05),
    POWER("Потребление", "Вт", 0.2, 8.0, 0.9, 1.1);

    private final String label;
    private final String unit;

    private final double min;
    private final double max;

    private final double minSolo;
    private final double maxSolo;
}
