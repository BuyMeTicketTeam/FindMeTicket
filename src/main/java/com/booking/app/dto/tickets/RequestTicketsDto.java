package com.booking.app.dto.tickets;

import com.booking.app.deserializers.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.booking.app.constants.DatePatternsConstants.DD_MM_YYYY_PATTERN;

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

    @Schema(description = "Date of departure in the format dd.MM.yyyy", example = "01.06.2024")
    @FutureOrPresent
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @DateTimeFormat(pattern = DD_MM_YYYY_PATTERN)
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
