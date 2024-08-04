package com.booking.app.dto.tickets;

import com.booking.app.deserializers.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "City of departure", example = "New York")
    @NotBlank
    private String departureCity;

    @Schema(description = "City of arrival", example = "Los Angeles")
    @NotBlank
    private String arrivalCity;

    @Schema(description = "Date of departure in the format dd.MM.yyyy", example = "01.06.2024")
    @FutureOrPresent
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @NotNull
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
