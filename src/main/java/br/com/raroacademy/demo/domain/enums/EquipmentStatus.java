package br.com.raroacademy.demo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EquipmentStatus {
    IN_USE("In use"),
    DEFECTIVE("Defective"),
    AVAILABLE("Available");
    private final String label;

    EquipmentStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
