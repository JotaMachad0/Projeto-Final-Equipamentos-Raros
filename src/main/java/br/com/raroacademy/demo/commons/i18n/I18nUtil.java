package br.com.raroacademy.demo.commons.i18n;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@AllArgsConstructor
@Component
public class I18nUtil {
    private final MessageSource messageSource;

    public String getMessage(String message, Object... args) {
        var locale = LocaleContextHolder.getLocale();
        return getMessage(message, locale, args);
    }

    public String getMessage(String message, Locale locale, Object... args) {
        return messageSource.getMessage(message, args, locale);
    }
}