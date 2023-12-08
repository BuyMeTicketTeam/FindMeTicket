package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

@Data
@AllArgsConstructor
@Builder
public class TicketDTO {
    String placeFrom;
    String placeAt;
    String departureCity;
    String arrivalCity;
    String departureTime;
    String departureDate;
    String arrivalTime;
    String arrivalDate;
    String travelTime;
    String price;
    //String url;
}
