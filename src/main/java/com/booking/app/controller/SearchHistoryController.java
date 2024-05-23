package com.booking.app.controller;

import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.UserCredentials;
import com.booking.app.services.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for managing user search history.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "History", description = "User's history")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    /**
     * Retrieves the user's search history.
     *
     * @param userCredentials The user credentials obtained from authentication.
     * @param request         The HTTP request containing language information.
     * @return ResponseEntity containing the user's search history.
     */
    @GetMapping("/getHistory")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "User's history", description = "Searching ticket of user history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully returned",
                    content = {@Content(schema = @Schema(implementation = SearchHistoryDto.class), mediaType = "application/json")}
            ),
            @ApiResponse(responseCode = "404", description = "History is not found")
    })
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal UserCredentials userCredentials, HttpServletRequest request) {
        return ResponseEntity.ok().body(searchHistoryService.getUserHistory(userCredentials, request.getHeader(HttpHeaders.CONTENT_LANGUAGE)));
    }

}
