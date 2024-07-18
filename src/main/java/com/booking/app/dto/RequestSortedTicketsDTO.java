package com.booking.app.dto;

import com.booking.app.constant.SortCriteria;
import com.booking.app.constant.SortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestSortedTicketsDTO {
    private String departureCity;
    private String arrivalCity;
    private String departureDate;
    private Map<SortCriteria, SortType> sortingBy;
    private boolean ascending;
    private Boolean bus;
    private Boolean train;
    private Boolean airplane;
    private Boolean ferry;
}
