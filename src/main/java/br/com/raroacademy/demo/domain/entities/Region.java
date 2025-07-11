package br.com.raroacademy.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static Region fromValue(String value) {
        for (Region region : Region.values()) {
            if (region.label.equalsIgnoreCase(value)) {
                return region;
            }
        }
        throw new IllegalArgumentException("Região inválida. As opções válidas são: " +
                "Norte, Nordeste, Centro-Oeste, Sudeste e Sul");
    }
}
