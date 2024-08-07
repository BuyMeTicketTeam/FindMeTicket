package com.booking.app.utils.formatters;

import com.booking.app.constants.ContentLanguage;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class TravelTimeFormatter {

    public String format(Duration travelTime, String language) {
        long hours = travelTime.toHours();
        long minutes = travelTime.toMinutes() % 60;

        ContentLanguage contentLanguage = ContentLanguage.fromCode(language);

        if (hours == 0) {
            return switch (contentLanguage) {
                case UA -> String.format("%sхв", minutes);
                case ENG -> String.format("%smin", minutes);
            };
        } else {
            return switch (contentLanguage) {
                case UA -> String.format("%sгод %sхв", hours, minutes);
                case ENG -> String.format("%sh %smin", hours, minutes);
            };
        }
    }

}
