package com.booking.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for requesting tickets.")
public class RequestTicketsDto {

    @NotNull
    @Schema(description = "City of departure", example = "New York")
    private String departureCity;

    @NotNull
    @Schema(description = "City of arrival", example = "Los Angeles")
    private String arrivalCity;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4}", message = "Date must be in the format dd.MM.yyyy")
    @Schema(description = "Date of departure in the format dd.MM.yyyy", example = "01.06.2024")
    private LocalDate departureDate;

    @NotNull
    @Builder.Default
    @Schema(description = "Indicates if the travel is by bus", example = "false")
    private Boolean bus = false;

    @NotNull
    @Builder.Default
    @Schema(description = "Indicates if the travel is by train", example = "false")
    private Boolean train = false;

    @NotNull
    @Builder.Default
    @Schema(description = "Indicates if the travel is by airplane", example = "true")
    private Boolean airplane = false;

    @NotNull
    @Builder.Default
    @Schema(description = "Indicates if the travel is by ferry", example = "true")
    private Boolean ferry = false;
}
