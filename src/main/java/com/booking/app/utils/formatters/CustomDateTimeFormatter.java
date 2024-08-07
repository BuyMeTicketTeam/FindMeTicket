package com.booking.app.utils.formatters;

import com.booking.app.constants.ContentLanguage;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class CustomDateTimeFormatter {

    private final String PATTERN = "d.MM, E";

    public String formatDate(LocalDate date, String language) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(PATTERN);

        ContentLanguage contentLanguage = ContentLanguage.fromCode(language);
        Locale locale = switch (contentLanguage) {
            case UA -> Locale.of("uk", "UA");
            case ENG -> Locale.ENGLISH;
        };

        formatter = formatter.withLocale(locale);

        return date.format(formatter);
    }

    public String formatTime(Instant arrivalDateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(arrivalDateTime, ZoneId.systemDefault());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        return localDateTime.format(timeFormatter);
    }

}
