package com.booking.app.dto.tickets;

import com.booking.app.constants.SortCriteria;
import com.booking.app.constants.SortType;
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
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "Data Transfer Object for requesting sorted tickets.")
public class RequestSortedTicketsDto {

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

    @Schema(description = "Sorting criteria", example = "price")
    private Map<SortCriteria, SortType> sortingBy;

    @Schema(description = "Indicates if the travel is by bus", example = "true")
    private Boolean bus;

    @Schema(description = "Indicates if the travel is by train", example = "true")
    private Boolean train;

    @Schema(description = "Indicates if the travel is by airplane", example = "false")
    private Boolean airplane;

    @Schema(description = "Indicates if the travel is by ferry", example = "false")
    private Boolean ferry;
}
