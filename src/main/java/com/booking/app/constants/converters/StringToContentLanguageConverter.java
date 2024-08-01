package com.booking.app.constants.converters;

import com.booking.app.constants.ContentLanguage;
import com.booking.app.exceptions.badrequest.UndefinedLanguageException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToContentLanguageConverter implements Converter<String, ContentLanguage> {

    @Override
    public ContentLanguage convert(String source) {
        try {
            return ContentLanguage.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UndefinedLanguageException();
        }
    }

}
