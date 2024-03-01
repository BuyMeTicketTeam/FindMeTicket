package com.booking.app.controller.api;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
@Tag(name = "Typing ahead",description = "Endpoint for the type-ahead feature for the search field")
public interface TypeAheadAPI {
    @Operation(summary = "Type ahead feature", description = "Find cities based on type-ahead search.")
    @ApiResponse(responseCode = "200", description = "Successful operation. Returns a list of cities.")
    ResponseEntity<List<CitiesDTO>> getCities(@NotNull @Valid StartLettersDTO startLetters,HttpServletRequest request) throws IOException;

}
