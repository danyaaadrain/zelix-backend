package ru.outofmemory.zelixbackend.utilities;

import lombok.Getter;

public enum MinerType {
    ANTMINER_L9("Antminer L9");

    @Getter
    private final String name;

    MinerType(String s) {
        this.name = s;
    }
}
