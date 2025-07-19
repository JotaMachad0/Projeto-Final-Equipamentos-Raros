package br.com.raroacademy.demo.domain.helpers;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.enums.Status;
import br.com.raroacademy.demo.exception.InvalidArgumentException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StatusHelper {

    public static Status fromLabelOrThrow(String label, I18nUtil i18nUtil) {
        for (Status status : Status.values()) {
            if (status.getLabel().equalsIgnoreCase(label)) {
                return status;
            }
        }

        String validStatuses = Arrays.stream(Status.values())
                .map(s -> i18nUtil.getMessage("expected.hiring.status." + s.name()))
                .collect(Collectors.joining(", "));

        throw new InvalidArgumentException(
                i18nUtil.getMessage("expected.hiring.invalid.status", validStatuses)
        );
    }
}
