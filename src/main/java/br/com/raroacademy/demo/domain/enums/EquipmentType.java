package br.com.raroacademy.demo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EquipmentType {
    HDMI_CABLE("HDMI Cable"),
    ANDROID_PHONE_CHARGER("Android Phone Charger"),
    IPHONE_CHARGER("iPhone Charger"),
    NOTEBOOK_CHARGER("Notebook Charger"),
    ANDROID_PHONE("Android Phone"),
    IPHONE("iPhone"),
    COMPUTER("Computer"),
    HEADPHONE("Headphone"),
    MONITOR("Monitor"),
    MOUSE("Mouse"),
    NOTEBOOK("Notebook"),
    KEYBOARD("Keyboard");

    private final String label;

    EquipmentType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public static EquipmentType fromLabel(String label) {
        for (EquipmentType type : values()) {
            if (type.label.equalsIgnoreCase(label) || type.name().equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo inválido: " + label);
    }
}