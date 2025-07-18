package br.com.raroacademy.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StockAlertStatus {
    Criado("Criada"),
    Processado("Processado"),
    Resolvido("Resolvido");

    private final String label;

    StockAlertStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
