package br.com.raroacademy.demo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    Criada("Criada"),
    Processada("Processada"),
    Concluída("Concluída"),
    Vencida("Vencida");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
