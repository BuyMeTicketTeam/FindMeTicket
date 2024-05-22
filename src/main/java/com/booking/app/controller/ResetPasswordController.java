package com.booking.app.controller;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.services.ResetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.booking.app.constant.ResetPasswordConstantMessages.*;

@RestController
@RequestMapping
@AllArgsConstructor
@Log4j2
@Tag(name = "Password Management", description = "Endpoints over password operations")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    /**
     * Sends a reset code to the specified email.
     *
     * @param dto          the email data transfer object containing the email address
     * @param siteLanguage the language preference for the email content
     * @return a ResponseEntity indicating the result of the operation
     * @throws MessagingException if there is an error while sending the email
     */
    @PostMapping("/reset")
    @Operation(summary = "Send reset code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_CODE_HAS_BEEN_SENT),
            @ApiResponse(responseCode = "404", description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED)
    })
    public ResponseEntity<?> sendResetCode(@RequestBody @NotNull @Valid EmailDTO dto,
                                           @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage) throws MessagingException {
        resetPasswordService.sendCode(dto.getEmail(), siteLanguage);
        return ResponseEntity.ok(MESSAGE_CODE_HAS_BEEN_SENT);
    }

    /**
     * Confirms the reset password token and updates the password.
     *
     * @param dto the reset password data transfer object containing the email, token, and new password
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/new-password")
    @Operation(summary = "Confirmation reset password token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET),
            @ApiResponse(responseCode = "400", description = MESSAGE_WRONG_CONFIRMATION_CODE_IS_PROVIDED),
            @ApiResponse(responseCode = "404", description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED)
    })
    public ResponseEntity<?> confirmResetPassword(@RequestBody @NotNull @Valid ResetPasswordDTO dto) {
        if (resetPasswordService.resetPassword(dto)) {
            return ResponseEntity.ok(MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MESSAGE_WRONG_CONFIRMATION_CODE_IS_PROVIDED);
        }
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
            @ApiResponse(responseCode = "400", description = MESSAGE_NEW_PASSWORDS_DO_NOT_MATCH)
    })
    public ResponseEntity<?> updatePassword(@RequestBody @NotNull @Valid RequestUpdatePasswordDTO updatePasswordDTO,
                                            @AuthenticationPrincipal UserCredentials userCredentials) {
        if (resetPasswordService.changePassword(updatePasswordDTO, userCredentials)) {
            return ResponseEntity.ok().body(MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED);
        } else {
            return ResponseEntity.badRequest().body(MESSAGE_NEW_PASSWORDS_DO_NOT_MATCH);
        }
    }

    /**
     * Resends a new reset code to the specified email.
     *
     * @param siteLanguage the language preference for the email content
     * @param dto          the token confirmation data transfer object containing the email address
     * @return a ResponseEntity indicating the result of the operation
     * @throws MessagingException if there is an error while sending the email
     */
    @Operation(summary = "Resend new reset code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_CODE_HAS_BEEN_SENT),
            @ApiResponse(responseCode = "404", description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED)
    })
    @PostMapping("/resend/reset-token")
    public ResponseEntity<?> resendResetPasswordToken(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                                      @RequestBody @NotNull @Valid TokenConfirmationDTO dto) throws MessagingException {
        if (resetPasswordService.sendCode(dto.getEmail(), siteLanguage)) {
            return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent one more time");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED);
    }

}
