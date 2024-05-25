package com.booking.app.controller;

import com.booking.app.dto.CodeConfirmationDTO;
import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.PasswordDto;
import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.ErrorDetails;
import com.booking.app.services.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.booking.app.constant.PasswordConstantMessages.*;
import static com.booking.app.exception.exception.InvalidConfirmationCodeException.MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED;

@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "Password Management", description = "Endpoints over password operations")
public class PasswordController {

    private final PasswordService passwordService;

    /**
     * Sends a reset code to the specified email.
     *
     * @param dto          the email data transfer object containing the email address
     * @param siteLanguage the language preference for the email content
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/reset")
    @Operation(summary = "Send a code", description = "Send confirmation code to mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_CODE_HAS_BEEN_SENT),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> sendResetCode(@RequestBody @NotNull @Valid EmailDTO dto,
                                           @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage) {
        passwordService.sendResetCode(dto.getEmail(), siteLanguage);
        return ResponseEntity.ok(MESSAGE_CODE_HAS_BEEN_SENT);
    }

    /**
     * Confirms the reset password token and updates the password.
     *
     * @param dto the reset password data transfer object containing the email, token, and new password
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/new-password")
    @Operation(summary = "Create new password", description = "Confirm code and set new password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET),
            @ApiResponse(responseCode = "400",
                    description = MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> confirmResetPassword(@RequestBody @NotNull @Valid PasswordDto dto) {
        passwordService.resetPassword(dto);
        return ResponseEntity.ok(MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET);
    }

    /**
     * Updates the password for an authenticated user.
     *
     * @param updatePasswordDTO the request update password data transfer object containing the old and new passwords
     * @param userCredentials   the authenticated user's credentials
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/update-password")
    @Operation(summary = "Update password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED),
            @ApiResponse(responseCode = "400",
                    description = MESSAGE_CURRENT_PASSWORD_IS_NOT_RIGHT,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> updatePassword(@RequestBody @NotNull @Valid RequestUpdatePasswordDTO updatePasswordDTO,
                                            @AuthenticationPrincipal UserCredentials userCredentials) {
        passwordService.changePassword(updatePasswordDTO, userCredentials);
        return ResponseEntity.ok().body(MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED);
    }

    /**
     * Resends a new reset code to the specified email.
     *
     * @param siteLanguage the language preference for the email content
     * @param dto          the token confirmation data transfer object containing the email address
     * @return a ResponseEntity indicating the result of the operation
     */
    @Operation(summary = "Resend new reset code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_CODE_HAS_BEEN_SENT),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    @PostMapping("/resend/reset-token")
    public ResponseEntity<?> resendResetPasswordToken(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                                      @RequestBody @NotNull @Valid CodeConfirmationDTO dto) {
        passwordService.sendResetCode(dto.getEmail(), siteLanguage);
        return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent one more time");
    }

}
