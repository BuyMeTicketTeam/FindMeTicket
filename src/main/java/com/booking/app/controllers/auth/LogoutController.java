package com.booking.app.controllers.auth;

import com.booking.app.annotations.GlobalApiResponses;
import com.booking.app.utils.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.app.constants.AuthenticatedUserConstants.*;

/**
 * Controller handling user logout functionality.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Log out", description = "Log out authenticated user")
@GlobalApiResponses
public class LogoutController {

    @GetMapping("/auth/sign-out")
    @Operation(summary = "Log out a user",
            description = "Logs out the authenticated user by clearing the authentication cookies",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully logged out",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookies(request, response, REFRESH_TOKEN, USER_ID, REMEMBER_ME);
    }

}
