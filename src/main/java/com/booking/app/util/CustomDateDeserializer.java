package com.booking.app.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getText();
        try {

            java.util.Date utilDate = dateFormat.parse(dateStr);

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date currentDate = new Date(System.currentTimeMillis());
            if (sqlDate.after(currentDate)) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }
            return sqlDate;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
}