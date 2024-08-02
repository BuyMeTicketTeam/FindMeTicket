package com.booking.app.controllers.user;

import com.booking.app.annotations.GlobalApiResponses;
import com.booking.app.constants.ApiMessagesConstants;
import com.booking.app.constants.RegistrationConstantMessages;
import com.booking.app.dto.ErrorDetailsDto;
import com.booking.app.dto.users.PasswordDto;
import com.booking.app.dto.users.RequestUpdatePasswordDTO;
import com.booking.app.entities.user.User;
import com.booking.app.services.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.booking.app.constants.PasswordConstantMessages.*;
import static com.booking.app.exceptions.badrequest.InvalidConfirmationCodeException.MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Operations over user's password")
@GlobalApiResponses
public class PasswordController {

    private final PasswordService passwordService;

    @PatchMapping("/{userId}/password/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Create new password",
            description = "Confirm code and set new password",
            responses = {
                    @ApiResponse(responseCode = "200", description = MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET, content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", description = MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED, content = {@Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", description = RegistrationConstantMessages.THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE, content = {@Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            })
    public void confirmResetPassword(@PathVariable("userId") String userId,
                                     @RequestBody @NotNull @Valid PasswordDto dto) {
        passwordService.resetPassword(dto);
    }

    @PatchMapping("/{userId}/password/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Update password",
            description = "Create new password for logged-in user",
            parameters = {
                    @Parameter(name = HttpHeaders.AUTHORIZATION, in = ParameterIn.HEADER, required = true, description = "Provide access JWT token",
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
                    @Parameter(name = "RefreshToken", in = ParameterIn.COOKIE, required = true, description = "Provide refresh JWT token",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED, content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = ApiMessagesConstants.UNAUTHENTICATED_MESSAGE, content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = MESSAGE_CURRENT_PASSWORD_IS_NOT_RIGHT, content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public void updatePassword(@PathVariable("userId") String userId,
                               @RequestBody @NotNull @Valid RequestUpdatePasswordDTO dto,
                               @AuthenticationPrincipal User user) {
        passwordService.changePassword(dto, user);
    }

}
