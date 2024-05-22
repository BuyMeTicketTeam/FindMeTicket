package com.booking.app.controller;

import com.booking.app.dto.AuthorizedUserDTO;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.OAuth2IdTokenDTO;
import com.booking.app.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling login requests, both basic and social login.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Login", description = "Basic and socials login")
public class LoginController {

    public static final String CLIENT_ID_IS_NOT_RIGHT_MESSAGE = "Client ID is not right";
    public static final String USER_HAS_BEEN_AUTHENTICATED = "User has been authenticated";
    public static final String NO_SOCIAL_IDENTITY_PROVIDERS_MESSAGE = "No Social Identity Providers was chosen";

    private final LoginService loginService;

    /**
     * Handles basic authentication.
     *
     * @param loginDTO the login data transfer object containing the user's email and password
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return a ResponseEntity containing the authentication result:
     * - HTTP 200 OK with the user details if authentication is successful
     * - HTTP 401 Unauthorized if authentication fails
     */
    @PostMapping("/login")
    @Operation(summary = "Basic authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = USER_HAS_BEEN_AUTHENTICATED,
                    content = {@Content(schema = @Schema(implementation = AuthorizedUserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or such user doesn't exist")
    })
    public ResponseEntity<?> basicLogin(@RequestBody @Valid @NotNull LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        return loginService.loginWithEmailAndPassword(loginDTO, request, response);
    }

    /**
     * Handles social authentication via OAuth2 providers like Google and Facebook.
     *
     * @param tokenDTO the OAuth2 token data transfer object containing the token from the social provider
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return a ResponseEntity containing the authentication result:
     * - HTTP 200 OK with the user details if authentication is successful
     * - HTTP 401 Unauthorized if the client ID is incorrect
     * - HTTP 400 Bad Request if no social identity providers were chosen
     */
    @Operation(summary = "Social authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = USER_HAS_BEEN_AUTHENTICATED,
                    content = {@Content(schema = @Schema(implementation = AuthorizedUserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = CLIENT_ID_IS_NOT_RIGHT_MESSAGE),
            @ApiResponse(responseCode = "400", description = NO_SOCIAL_IDENTITY_PROVIDERS_MESSAGE)
    })
    @PostMapping("/oauth2/authorize/*")
    public ResponseEntity<?> socialLogin(@RequestBody @Valid @NotNull OAuth2IdTokenDTO tokenDTO, HttpServletRequest request, HttpServletResponse response) {
        if (request.getRequestURI().contains("google")) {
            return loginService.loginWithGoogle(tokenDTO, request, response);
        } else if (request.getRequestURI().contains("facebook")) {
            // TODO complete authentication via Facebook
        }
        return ResponseEntity.badRequest().body(NO_SOCIAL_IDENTITY_PROVIDERS_MESSAGE);
    }

}
