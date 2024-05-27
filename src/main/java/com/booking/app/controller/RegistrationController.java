package com.booking.app.controller;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.CodeConfirmationDTO;
import com.booking.app.exception.ErrorDetails;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.booking.app.constant.RegistrationConstantMessages.*;

/**
 * REST controller for handling user registration and email confirmation.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "Registration", description = "Endpoints for registration and confirmation")
public class RegistrationController {

    private final RegistrationService registrationService;

    private final MailSenderService mailSenderService;

    /**
     * Registers a new user.
     *
     * @param siteLanguage the language of the site
     * @param dto          the registration data transfer object
     * @return a ResponseEntity containing the email data transfer object
     * @throws MessagingException if an error occurs while sending the email
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Register a user", description = "Attempt to sign up new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = USER_REGISTERED_SUCCESSFULLY_MESSAGE + "\t\n"
                            + EMAIL_IS_ALREADY_TAKEN_MESSAGE)})
    public ResponseEntity<?> signUp(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                    @RequestBody @Valid @NotNull RegistrationDTO dto) throws MessagingException {
        EmailDTO register = registrationService.register(dto, siteLanguage);
        return ResponseEntity.ok().body(register);
    }

    /**
     * Confirms the user's email using a code.
     *
     * @param dto the code confirmation data transfer object
     * @return a ResponseEntity containing the result of the confirmation
     */
    @PostMapping("/verify")
    @Operation(summary = "Email confirmation", description = "Confirm user identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = USER_IS_VERIFIED_MESSAGE),
            @ApiResponse(responseCode = "400",
                    description = CODE_IS_NOT_RIGHT_MESSAGE,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> confirmEmailCode(@RequestBody @NotNull @Valid CodeConfirmationDTO dto) {
        registrationService.confirmCode(dto);
        return ResponseEntity.status(HttpStatus.OK).body(USER_IS_VERIFIED_MESSAGE);
    }

    /**
     * Resends the email confirmation code.
     *
     * @param siteLanguage the language of the site
     * @param dto          the code confirmation data transfer object
     * @return a ResponseEntity containing the result of the resend operation
     */
    @PostMapping("/resend/verification-code")
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
