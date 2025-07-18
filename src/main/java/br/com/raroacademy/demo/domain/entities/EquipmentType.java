package br.com.raroacademy.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum EquipmentType {
    Cabo_HDMI("Cabo HDMI", 10, 15),
    Carregador_de_celular_Android("Carregador de celular Android", 5, 10),
    Carregador_de_celular_iPhone("Carregador de celular iPhone",2, 5),
    Carregador_de_notebook("Carregador de notebook", 10, 15),
    Celular_Android("Celular Android", 10, 15),
    Celular_iPhone("Celular iPhone", 5, 10),
    Computador("Computador", 10, 20),
    Fone_de_ouvido("Fone de ouvido",15,25),
    Monitor("Monitor",15,25),
    Mouse("Mouse", 20, 30),
    Notebook("Notebook", 20, 30),
    Teclado("Teclado", 20, 30);

    private final String label;
    private final int minimumStock;
    private final int securityStock;

    EquipmentType(String label, int minimumStock, int securityStock) {
        this.label = label;
        this.minimumStock = minimumStock;
        this.securityStock = securityStock;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
