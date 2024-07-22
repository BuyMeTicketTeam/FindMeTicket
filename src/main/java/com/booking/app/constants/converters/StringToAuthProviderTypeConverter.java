package com.booking.app.constants.converters;

import com.booking.app.entities.user.AuthProvider;
import com.booking.app.exceptions.user.InvalidSocialProviderException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAuthProviderTypeConverter implements Converter<String, AuthProvider.AuthProviderType> {
    @Override
    public AuthProvider.AuthProviderType convert(String source) {
        try {
            return AuthProvider.AuthProviderType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSocialProviderException();
        }
    }
}
