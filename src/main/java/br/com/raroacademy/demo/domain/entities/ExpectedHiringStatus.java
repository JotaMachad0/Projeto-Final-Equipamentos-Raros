package br.com.raroacademy.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpectedHiringStatus {
    Criada("Criada"),
    Processada("Processada"),
    Concluída("Concluída"),
    Vencida("Vencida");

    private final String label;

    ExpectedHiringStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
