package com.booking.app.entities.converters;

import com.booking.app.constants.TransportType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransportTypeConverter implements AttributeConverter<TransportType, String> {

    @Override
    public String convertToDatabaseColumn(TransportType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TransportType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TransportType.valueOf(dbData);
    }

}