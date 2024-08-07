package com.booking.app.entities.converters;


import com.booking.app.constants.Website;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WebsiteConverter implements AttributeConverter<Website, String> {

    @Override
    public String convertToDatabaseColumn(Website attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public Website convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Website.valueOf(dbData);
    }

}