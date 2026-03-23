package ru.outofmemory.zelixbackend.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MinerAlgo {
    BTC("TH/s"),
    LTC("GH/s");

    private final String unit;
}
