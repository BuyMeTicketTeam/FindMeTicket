package com.booking.app.controllers.testUtils;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class DateTimeUtils {

    public Duration countTravelTime(LocalDate departureDate, LocalTime departureTime, Instant arrivalDateTime) {
        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime dateTime = LocalDateTime.ofInstant(arrivalDateTime, zoneId);

        return Duration.between(departureDateTime, dateTime);
    }

    public LocalTime between(LocalTime startTime, LocalTime endTime) {
        int startSeconds = startTime.toSecondOfDay();
        int endSeconds = endTime.toSecondOfDay();
        int randomTime = ThreadLocalRandom
                .current()
                .nextInt(startSeconds, endSeconds);

        return LocalTime.ofSecondOfDay(randomTime);
    }

    public Instant getRandomInstantWithinThreeDays() {
        Instant now = Instant.now();
        Instant threeDaysFromNow = now.plus(Duration.ofDays(3));

        long randomEpochSecond = ThreadLocalRandom.current().nextLong(now.getEpochSecond(), threeDaysFromNow.getEpochSecond());
        return Instant.ofEpochSecond(randomEpochSecond);
    }
}
