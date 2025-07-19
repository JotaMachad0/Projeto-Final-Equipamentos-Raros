package br.com.raroacademy.demo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PurchaseStatus {
    PURCHASED("purchased"),
    ARRIVED_FROM("arrived from"),
    REGISTERED("registered");

    private final String label;

    PurchaseStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}