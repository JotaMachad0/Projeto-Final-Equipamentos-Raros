package br.com.raroacademy.demo.domain.enums;

import java.util.Map;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toMap;

public enum Region {

    CENTRO_OESTE( 8, "DF", "GO", "MT", "MS"),
    NORDESTE( 10, "AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE"),
    NORTE(15, "AC", "AP", "AM", "PA", "RO", "RR", "TO"),
    SUDESTE(5, "ES", "MG", "RJ", "SP"),
    SUL( 7, "PR", "RS", "SC");

    private final int deliveryTimeInDays;
    private final String[] states;

    private static final Map<String, Region> STATE_REGION_MAP =
            Stream.of(values()).flatMap(r -> Stream.of(r.states).map(estado -> Map.entry(estado, r)))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    Region(int deliveryTimeInDays, String... states) {
        this.deliveryTimeInDays = deliveryTimeInDays;
        this.states = states;
    }

    public static Region fromEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return SUDESTE;
        }
        return STATE_REGION_MAP.getOrDefault(estado.toUpperCase(), SUDESTE);
    }

    public int getPrazoEmDias() {
        return deliveryTimeInDays;
    }
}
