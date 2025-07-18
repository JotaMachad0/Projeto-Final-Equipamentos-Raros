package br.com.raroacademy.demo.domain.helpers;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.ExpectedHiringStatus;
import br.com.raroacademy.demo.exception.InvalidArgumentException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StatusHelper {

    public static ExpectedHiringStatus fromLabelOrThrow(String label, I18nUtil i18nUtil) {
        for (ExpectedHiringStatus expectedHiringStatus : ExpectedHiringStatus.values()) {
            if (expectedHiringStatus.getLabel().equalsIgnoreCase(label)) {
                return expectedHiringStatus;
            }
        }

        String validStatuses = Arrays.stream(ExpectedHiringStatus.values())
                .map(s -> i18nUtil.getMessage("expected.hiring.status." + s.name()))
                .collect(Collectors.joining(", "));

        throw new InvalidArgumentException(
                i18nUtil.getMessage("expected.hiring.invalid.status", validStatuses)
        );
    }
}
