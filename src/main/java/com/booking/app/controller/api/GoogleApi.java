package com.booking.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public interface GoogleApi {
    @Operation(summary = "Make authentication by Google")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", message = HttpStatuses.OK, response = SuccessSignInDto.class),
            @ApiResponse(responseCode = "400", message = BAD_GOOGLE_TOKEN)
    })
    @GetMapping
    public SuccessSignInDto authenticate(@RequestParam @NotBlank String idToken);
}
