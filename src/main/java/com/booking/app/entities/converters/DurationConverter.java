package com.booking.app.entities.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute == null ? null : attribute.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : Duration.ofMinutes(dbData);
    }

}