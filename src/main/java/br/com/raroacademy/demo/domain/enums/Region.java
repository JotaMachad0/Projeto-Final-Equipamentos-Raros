package br.com.raroacademy.demo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Map;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toMap;

public enum Region {

    CENTRO_OESTE("Centro-Oeste", 8, "DF", "GO", "MT", "MS"),
    NORDESTE("Nordeste", 10, "AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE"),
    NORTE("Norte", 15, "AC", "AP", "AM", "PA", "RO", "RR", "TO"),
    SUDESTE("Sudeste", 5, "ES", "MG", "RJ", "SP"),
    SUL("Sul", 7, "PR", "RS", "SC");

    private final String label;
    private final int deliveryTimeInDays;
    private final String[] states;

    private static final Map<String, Region> STATE_REGION_MAP =
            Stream.of(values()).flatMap(r -> Stream.of(r.states).map(estado -> Map.entry(estado, r)))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    Region(String label, int deliveryTimeInDays, String... states) {
        this.label = label;
        this.deliveryTimeInDays = deliveryTimeInDays;
        this.states = states;
    }

    public static Region fromEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return SUDESTE;
        }
        return STATE_REGION_MAP.getOrDefault(estado.toUpperCase(), SUDESTE);
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public int getPrazoEmDias() {
        return deliveryTimeInDays;
    }
}