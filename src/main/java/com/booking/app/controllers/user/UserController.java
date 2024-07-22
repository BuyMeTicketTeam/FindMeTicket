package com.booking.app.controllers.user;

import com.booking.app.annotations.GlobalApiResponses;
import com.booking.app.constants.ApiMessagesConstants;
import com.booking.app.entities.user.User;
import com.booking.app.exceptions.ErrorDetailsDto;
import com.booking.app.services.UserService;
import com.booking.app.utils.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.booking.app.constants.AuthenticatedUserConstants.*;

/**
 * REST controller for managing user-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints for user management")
@GlobalApiResponses
public class UserController {

    private final UserService userService;


    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Delete a user",
            description = "Deletes the authenticated user account",
            parameters = {
                    @Parameter(name = HttpHeaders.AUTHORIZATION, in = ParameterIn.HEADER, required = true, description = "Provide access JWT token",
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
                    @Parameter(name = "RefreshToken", in = ParameterIn.COOKIE, required = true, description = "Provide refresh JWT token",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = ApiMessagesConstants.USER_HAS_BEEN_DELETED_MESSSAGE, content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = ApiMessagesConstants.UNAUTHENTICATED_MESSAGE, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDetailsDto.class)))
            })
    public void deleteUser(@PathVariable("userId") String userId,
                           @AuthenticationPrincipal User user,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        userService.delete(user);
        CookieUtils.deleteCookies(request, response, REFRESH_TOKEN, USER_ID, REMEMBER_ME);
    }

}
