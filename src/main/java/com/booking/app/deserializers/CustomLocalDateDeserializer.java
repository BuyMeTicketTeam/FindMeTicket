package com.booking.app.deserializers;

import com.booking.app.exceptions.badrequest.InvalidDateFormatException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.booking.app.constants.DatePatternsConstants.DD_MM_YYYY_PATTERN;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DD_MM_YYYY_PATTERN);

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        try {
            return LocalDate.parse(date, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(DD_MM_YYYY_PATTERN) {
            };
        }
    }

}