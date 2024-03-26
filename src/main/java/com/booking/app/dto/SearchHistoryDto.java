package com.booking.app.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchHistoryDto {

    @NotNull
    private Long departureCityId;

    @NotNull
    private Long arrivalCityId;

    @NotNull
    private String departureDate;

    @NotNull
    private String addingTime;
}
