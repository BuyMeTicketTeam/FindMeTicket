package com.booking.app.dto;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class TicketDto {

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
}
