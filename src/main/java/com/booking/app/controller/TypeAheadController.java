package com.booking.app.controller;

import com.booking.app.controller.api.TypeAheadAPI;
import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import com.booking.app.services.TypeAheadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller handling type-ahead functionality for city search.
 */
@RestController
@AllArgsConstructor
@RequestMapping
public class TypeAheadController implements TypeAheadAPI {

    private TypeAheadService typeAheadService;

    /**
     * Endpoint to fetch cities based on provided start letters.
     *
     * @param startLetters DTO containing the start letters for city search.
     * @return ResponseEntity with a list of CitiesDTO as the response body.
     */
    @PostMapping("/typeAhead")
    @Override
    public ResponseEntity<List<CitiesDTO>> getCities(RequestTypeAheadDTO startLetters) {
        return ResponseEntity.ok().body(typeAheadService.findMatches(startLetters));
    }

}
