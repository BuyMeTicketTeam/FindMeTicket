package com.booking.app.controller;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;
import com.booking.app.services.TypeAheadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Controller handling type-ahead functionality for city search.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Typing ahead", description = "Endpoint for the type-ahead feature for the search field")
public class TypeAheadController {

    private final TypeAheadService typeAheadService;

    /**
     * Endpoint to fetch cities based on provided start letters.
     *
     * @param startLetters DTO containing the start letters for city search.
     * @return ResponseEntity with a list of CitiesDTO as the response body.
     */
    // todo ?startLetters=
    @GetMapping(path = "cities/typeahead")
    @Operation(summary = "Type ahead feature", description = "Find cities based on type-ahead search")
    @ApiResponse(responseCode = "200",
            description = "Successful operation. Returns a list of cities",
            content = {@Content(schema = @Schema(implementation = CitiesDTO.class), mediaType = "application/json")}
    )
    public ResponseEntity<List<CitiesDTO>> getCities(@RequestBody @NotNull @Valid StartLettersDTO startLetters, HttpServletRequest request) throws IOException {
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        return ResponseEntity.ok().body(typeAheadService.findMatches(startLetters, siteLanguage));
    }

}
