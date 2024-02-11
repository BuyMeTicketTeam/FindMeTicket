package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestSortedTicketsDTO {
    private String departureCity;
    private String arrivalCity;
    private String departureDate;
    private String sortingBy;//price, departureTime, arrivalTime, travelTime
    private boolean ascending;
    private Boolean bus;
    private Boolean train;
    private Boolean airplane;
    private Boolean ferry;
}
