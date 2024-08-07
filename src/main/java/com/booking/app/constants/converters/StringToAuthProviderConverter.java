package com.booking.app.constants.converters;

import com.booking.app.constants.AuthProvider;
import com.booking.app.exceptions.badrequest.InvalidSocialProviderException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAuthProviderConverter implements Converter<String, AuthProvider> {
    @Override
    public AuthProvider convert(String source) {
        try {
            return AuthProvider.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSocialProviderException();
        }
    }
}
