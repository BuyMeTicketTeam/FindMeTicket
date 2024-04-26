package com.booking.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4}", message = "Date must be in the format dd.MM.yyyy")
    private String departureDate;
    @NotNull
    @Builder.Default
    private Boolean bus = false;
    @NotNull
    @Builder.Default
    private Boolean train = false;
    @NotNull
    private Boolean airplane;
    @NotNull
    private Boolean ferry;
}
