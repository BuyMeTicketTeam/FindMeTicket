package com.booking.app.controller;

import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.ErrorDetails;
import com.booking.app.services.UserCredentialsService;
import com.booking.app.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.app.constant.CustomHttpHeaders.REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;

/**
 * REST controller for managing user-related operations.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "User", description = "User management")
public class UserController {
    public static final String MESSAGE_USER_HAS_BEEN_DELETED = "User has been deleted";
    public static final String MESSAGE_UNAUTHENTICATED = "Full authentication is required to access this resource";

    private final UserCredentialsService userCredentialsService;

    /**
     * Deletes the authenticated user.
     *
     * @param userCredentials the authenticated user's credentials
     * @param request         the HTTP request
     * @param response        the HTTP response
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping("/users")
    @Operation(summary = "Delete a user", description = "Delete authenticated user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "201",
                    description = MESSAGE_UNAUTHENTICATED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserCredentials userCredentials,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        userCredentialsService.delete(userCredentials);
        deleteCookies(request, response);
        return ResponseEntity.ok().body(MESSAGE_USER_HAS_BEEN_DELETED);
    }

    /**
     * Deletes cookies related to user authentication.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    private static void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.deleteCookie(request, response, USER_ID);
        CookieUtils.deleteCookie(request, response, REMEMBER_ME);
    }

}
