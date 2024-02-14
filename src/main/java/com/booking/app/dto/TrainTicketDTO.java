package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainTicketDTO {

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

    private BigDecimal priceMin;
    
}
