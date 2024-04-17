package com.booking.app.dto;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {

    private UUID id;

    private String type;

    private String placeFrom;
    private String placeAt;

    private String departureCity;
    private String arrivalCity;

    private String departureTime;
    private String departureDate;

    private String arrivalTime;
    private String arrivalDate;

    private String travelTime;

    private BigDecimal price;

    private String carrier;


}
