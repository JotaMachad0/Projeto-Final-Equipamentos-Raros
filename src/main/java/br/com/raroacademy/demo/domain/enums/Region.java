package br.com.raroacademy.demo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Region {
    Centro_Oeste("Centro-Oeste"),
    Nordeste("Nordeste"),
    Norte("Norte"),
    Sudeste("Sudeste"),
    Sul("Sul");

    private final String label;

    Region(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
