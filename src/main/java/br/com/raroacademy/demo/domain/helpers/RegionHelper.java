package br.com.raroacademy.demo.domain.helpers;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.Region;
import br.com.raroacademy.demo.exception.InvalidArgumentException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RegionHelper {

    public static Region fromLabelOrThrow(String label, I18nUtil i18nUtil) {
        for (Region region : Region.values()) {
            if (region.getLabel().equalsIgnoreCase(label)) {
                return region;
            }
        }

        String validRegions = Arrays.stream(Region.values())
                .map(r -> i18nUtil.getMessage("expected.hiring.region." + r.name()))
                .collect(Collectors.joining(", "));

        throw new InvalidArgumentException(
                i18nUtil.getMessage("expected.hiring.invalid.region", validRegions)
        );
    }
}
