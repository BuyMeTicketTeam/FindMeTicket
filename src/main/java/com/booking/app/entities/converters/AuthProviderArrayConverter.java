package com.booking.app.entities.converters;

import com.booking.app.constants.AuthProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class AuthProviderArrayConverter implements AttributeConverter<AuthProvider[], String[]> {

    @Override
    public String[] convertToDatabaseColumn(AuthProvider[] attribute) {
        if (attribute == null) {
            return null;
        }
        return Arrays.stream(attribute)
                .map(Enum::name)
                .toArray(String[]::new);
    }

    @Override
    public AuthProvider[] convertToEntityAttribute(String[] dbData) {
        if (dbData == null) {
            return null;
        }
        return Arrays.stream(dbData)
                .map(AuthProvider::valueOf)
                .toArray(AuthProvider[]::new);
    }
}
