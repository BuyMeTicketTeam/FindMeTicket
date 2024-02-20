package com.booking.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTicketsDTO {
    @NotNull
    private String departureCity;
    @NotNull
    private String arrivalCity;
    @NotNull
    private String departureDate;
    @NotNull
    private Boolean bus;
    @NotNull
    private Boolean train;
    @NotNull
    private Boolean airplane;
    @NotNull
    private Boolean ferry;
}
