package ru.outofmemory.zelixbackend.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MinerType {
    ANTMINER_L9("Antminer L9");

    private final String name;
}
