package com.booking.app.controller.api;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface TypeAheadAPI {
    @Operation(summary = "Type ahead feature", description = "Find cities based on type-ahead search.")
    @ApiResponse(responseCode = "200", description = "Successful operation. Returns a list of cities.")
    ResponseEntity<List<CitiesDTO>> getCities(@RequestBody @NotNull RequestTypeAheadDTO letters);

}
