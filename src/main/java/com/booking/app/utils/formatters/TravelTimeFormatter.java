package com.booking.app.utils.formatters;

import com.booking.app.constants.ContentLanguage;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Duration parse(String travelTime, ContentLanguage language) {
        String patternStr = switch (language) {
            case UA -> "\\s*(\\d+)год\\s*(\\d+)хв|\\s*(\\d+)хв|\\s*(\\d+)год";
            case ENG -> "(\\d+)h\\s*(\\d+)min|\\s*(\\d+)min|\\s*(\\d+)h";
        };

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(travelTime);

        if (matcher.find()) {
            long hours = matcher.group(1) != null ?
                    Long.parseLong(matcher.group(1)) : (matcher.group(4) != null ?
                    Long.parseLong(matcher.group(4)) : 0
            );
            long minutes = matcher.group(2) != null ?
                    Long.parseLong(matcher.group(2)) : (matcher.group(3) != null ?
                    Long.parseLong(matcher.group(3)) : 0
            );
            return Duration.ofHours(hours).plusMinutes(minutes);
        }

        throw new IllegalArgumentException("Invalid formatted duration string: " + travelTime);
    }

}
