package br.com.raroacademy.demo.domain.enums;

import lombok.Getter;

@Getter
public enum EquipmentType {
    HDMI_CABLE(10, 15),
    ANDROID_CELLPHONE_CHARGER(5, 10),
    IPHONE_CHARGER(2, 5),
    NOTEBOOK_CHARGER(10, 15),
    ANDROID_CELLPHONE(10, 15),
    IPHONE(5, 10),
    COMPUTER(10, 20),
    EARPHONES(15, 25),
    MONITOR(15, 25),
    MOUSE(20, 30),
    NOTEBOOK(20, 30),
    KEYBOARD(20, 30);

    private final int minimumStock;
    private final int securityStock;

    EquipmentType(int minimumStock, int securityStock) {
        this.minimumStock = minimumStock;
        this.securityStock = securityStock;
    }
}
