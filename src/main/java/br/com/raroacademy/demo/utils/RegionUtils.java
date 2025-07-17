package br.com.raroacademy.demo.utils;

public class RegionUtils {

    public enum Region {
        SUDESTE, SUL, CENTRO_OESTE, NORDESTE, NORTE_REMOTO
    }

    public static Region getRegionByUf(String uf) {
        return switch (uf.toUpperCase()) {
            case "MG", "SP", "RJ", "ES" -> Region.SUDESTE;
            case "PR", "SC", "RS" -> Region.SUL;
            case "DF", "GO", "MT", "MS" -> Region.CENTRO_OESTE;
            case "BA", "PE", "AL", "PB", "SE", "RN", "PI", "CE" -> Region.NORDESTE;
            default -> Region.NORTE_REMOTO;
        };
    }

    public static int getAvgDeliveryTimeByRegion(Region region) {
        return switch (region) {
            case SUDESTE -> 3;
            case SUL, CENTRO_OESTE, NORDESTE -> 7;
            case NORTE_REMOTO -> 10;
        };
    }
}
