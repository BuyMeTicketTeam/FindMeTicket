package com.booking.app.controller;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import com.booking.app.util.HtmlTemplateUtils;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.booking.app.constant.RegistrationConstantMessages.*;

/**
 * REST controller for handling user registration and email confirmation.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Log4j2
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
     * @throws IOException        if an I/O error occurs
     */
    @PostMapping("/register")
    @Operation(summary = "Register a user", description = "Attempt to sign up new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = USER_REGISTERED_SUCCESSFULLY_MESSAGE + "\t\n"
                            + EMAIL_IS_ALREADY_TAKEN_MESSAGE)})
    public ResponseEntity<?> signUp(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                    @RequestBody @Valid @NotNull RegistrationDTO dto) throws MessagingException, IOException {
        EmailDTO register = registrationService.register(dto, siteLanguage);
        log.info("User with email {} has successfully registered!", dto.getEmail());
        return ResponseEntity.ok().body(register);
    }

    /**
     * Confirms the user's email using a token.
     *
     * @param dto the token confirmation data transfer object
     * @return a ResponseEntity containing the result of the confirmation
     */
    @PostMapping("/confirm-email")
    @Operation(summary = "Email confirmation", description = "Confirm user identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = USER_IS_VERIFIED_MESSAGE),
            @ApiResponse(responseCode = "400", description = CODE_IS_NOT_RIGHT_MESSAGE)
    })
    public ResponseEntity<?> confirmEmailToken(@RequestBody @NotNull @Valid TokenConfirmationDTO dto) {
        if (registrationService.enableIfValid(dto)) {
            log.info(String.format("User %s has successfully confirmed email!", dto.getEmail()));
            return ResponseEntity.status(HttpStatus.OK).body(USER_IS_VERIFIED_MESSAGE);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CODE_IS_NOT_RIGHT_MESSAGE);
        }
    }

    /**
     * Resends the email confirmation token.
     *
     * @param siteLanguage the language of the site
     * @param dto          the token confirmation data transfer object
     * @return a ResponseEntity containing the result of the resend operation
     */
    @PostMapping("/resend/confirm-token")
    @Operation(summary = "Resend email confirmation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = CONFIRM_TOKEN_HAS_BEEN_SENT_ONE_MORE_TIME_MESSAGE),
            @ApiResponse(responseCode = "404", description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE),
    })
    public ResponseEntity<?> resendConfirmEmailToken(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                                     @RequestBody @NotNull @Valid TokenConfirmationDTO dto) {
        String htmlTemplate = HtmlTemplateUtils.getConfirmationHtmlTemplate(siteLanguage);
        if (mailSenderService.resendEmail(dto.getEmail(), htmlTemplate)) {
            return ResponseEntity.status(HttpStatus.OK).body(CONFIRM_TOKEN_HAS_BEEN_SENT_ONE_MORE_TIME_MESSAGE);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE);
        }
    }

}
