package com.booking.app.controllers.user;

import com.booking.app.annotations.GlobalApiResponses;
import com.booking.app.constants.ApiMessagesConstants;
import com.booking.app.constants.ContentLanguage;
import com.booking.app.dto.ErrorDetailsDto;
import com.booking.app.dto.users.HistoryDto;
import com.booking.app.entities.user.User;
import com.booking.app.services.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints for user management")
@GlobalApiResponses
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping("/{userId}/history")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "User history",
            description = "Routes from user history",
            parameters = {
                    @Parameter(name = HttpHeaders.AUTHORIZATION, in = ParameterIn.HEADER, required = true, description = "Provide access JWT token",
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
                    @Parameter(name = "RefreshToken", in = ParameterIn.COOKIE, required = true, description = "Provide refresh JWT token",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned", content = @Content(schema = @Schema(implementation = HistoryDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401", description = ApiMessagesConstants.UNAUTHENTICATED_MESSAGE, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDetailsDto.class)))
            })
    public List<HistoryDto> getHistory(@PathVariable("userId") String userId,
                                       @RequestHeader(name = HttpHeaders.CONTENT_LANGUAGE) ContentLanguage language,
                                       @AuthenticationPrincipal User user) {
        return searchHistoryService.getHistory(user, language.getLanguage());
    }

}
