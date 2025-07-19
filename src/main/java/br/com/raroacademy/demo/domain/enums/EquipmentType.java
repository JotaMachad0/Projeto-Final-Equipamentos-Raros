package br.com.raroacademy.demo.domain.enums;


public enum EquipmentType {
    CABO_HDMI("Cabo HDMI", 10, 15),
    CARREGADOR_CELULAR_ANDROID("Carregador de celular Android", 5, 10),
    CARREGADOR_CELULAR_IPHONE("Carregador de celular iPhone", 2, 5),
    CARREGADOR_NOTEBOOK("Carregador de notebook", 10, 15),
    CELULAR_ANDROID("Celular Android", 10, 15),
    CELULAR_IPHONE("Celular iPhone", 5, 10),
    COMPUTADOR("Computador", 10, 20),
    FONE_OUVIDO("Fone de ouvido", 15, 25),
    MONITOR("Monitor", 15, 25),
    MOUSE("Mouse", 20, 30),
    NOTEBOOK("Notebook", 20, 30),
    TECLADO("Teclado", 20, 30);

    private final String label;
    private final int minimumStock;
    private final int securityStock;

    EquipmentType(String label, int minimumStock, int securityStock) {
        this.label = label;
        this.minimumStock = minimumStock;
        this.securityStock = securityStock;
    }

    public String getLabel() {
        return label;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public int getSecurityStock() {
        return securityStock;
    }
}

