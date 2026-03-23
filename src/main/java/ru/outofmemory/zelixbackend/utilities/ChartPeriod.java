package ru.outofmemory.zelixbackend.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum ChartPeriod {
    HOURLY(12, Duration.ofMinutes(5)),
    DAILY(24, Duration.ofHours(1));

    private final int pointsNum;
    private final Duration delta;
}
