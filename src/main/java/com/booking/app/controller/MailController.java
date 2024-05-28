package com.booking.app.controller;

import com.booking.app.dto.CodeConfirmationDTO;
import com.booking.app.dto.EmailDTO;
import com.booking.app.exception.ErrorDetails;
import com.booking.app.services.MailSenderService;
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
import org.springframework.web.bind.annotation.*;

import static com.booking.app.constant.PasswordConstantMessages.MESSAGE_CODE_HAS_BEEN_SENT;
import static com.booking.app.constant.PasswordConstantMessages.THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED;
import static com.booking.app.constant.RegistrationConstantMessages.CONFIRM_CODE_HAS_BEEN_SENT_ONE_MORE_TIME_MESSAGE;
import static com.booking.app.constant.RegistrationConstantMessages.THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "Mail messages", description = "Endpoints for sending emails")
// TODO unite both requests and extract send() method from PasswordService into separated service (MAILservice)
public class MailController {

    private final PasswordService passwordService;
    private final MailSenderService mailSenderService;

    /**
     * Sends a reset code to the specified email.
     *
     * @param dto          the email data transfer object containing the email address
     * @param siteLanguage the language preference for the email content
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/reset-code/send")
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
     * Resends a new reset code to the specified email.
     *
     * @param siteLanguage the language preference for the email content
     * @param dto          the token confirmation data transfer object containing the email address
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/reset-code/send")
    @Operation(summary = "Resend new reset code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_CODE_HAS_BEEN_SENT),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> resendResetPasswordToken(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                                      @RequestBody @NotNull @Valid CodeConfirmationDTO dto) {
        passwordService.sendResetCode(dto.getEmail(), siteLanguage);
        return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent one more time");
    }

    /**
     * Resends the email confirmation code.
     *
     * @param siteLanguage the language of the site
     * @param dto          the code confirmation data transfer object
     * @return a ResponseEntity containing the result of the resend operation
     */
    @PostMapping("/verification-code/send")
    @Operation(summary = "Resend email confirmation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = CONFIRM_CODE_HAS_BEEN_SENT_ONE_MORE_TIME_MESSAGE),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> resendConfirmEmailCode(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                                    @RequestBody @NotNull @Valid CodeConfirmationDTO dto) {
        mailSenderService.resendEmail(siteLanguage, dto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(CONFIRM_CODE_HAS_BEEN_SENT_ONE_MORE_TIME_MESSAGE);
    }

}
