package com.booking.app.controller;

import com.booking.app.controller.api.RegisterAPI;
import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import com.booking.app.util.HtmlTemplateUtils;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * RegisterController handles user registration and email confirmation operations.
 * This controller provides endpoints for user registration, email confirmation, and resending confirmation email.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Log4j2

public class RegistrationController implements RegisterAPI {

    private final RegistrationService registrationService;

    private final MailSenderService mailSenderService;

    /**
     * Handles user registration request.
     *
     * @param dto The RegistrationDTO containing user registration information.
     * @return ResponseDTO containing information about the registration process.
     * @throws MessagingException Thrown if an error occurs during email sending.
     * @throws IOException        Thrown if an I/O error occurs.
     */
    @PostMapping("/register")
    @Override
    public ResponseEntity<?> signUp(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage, @RequestBody RegistrationDTO dto) throws MessagingException, IOException {
        EmailDTO register = registrationService.register(dto, siteLanguage);
        log.info(String.format("User %s has successfully registered!", dto.getEmail()));
        return ResponseEntity.ok().body(register);
    }

    /**
     * Handles user email confirmation request.
     *
     * @param dto The TokenConfirmationDTO containing the confirmation token.
     * @return ResponseEntity indicating the result of the email confirmation.
     */
    @PostMapping("/confirm-email")
    @Override
    public ResponseEntity<?> confirmEmailToken(@RequestBody TokenConfirmationDTO dto) {
        if (registrationService.enableIfValid(dto)) {
            log.info(String.format("User %s has successfully confirmed email!", dto.getEmail()));
            return ResponseEntity.status(HttpStatus.OK).body("User successfully confirmed its email");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not right");
    }

    /**
     * Handles the request to resend the confirmation token email.
     *
     * @param dto The TokenConfirmationDTO containing the user's email.
     * @return ResponseEntity indicating the result of the resend operation.
     * @throws MessagingException Thrown if an error occurs during email sending.
     * @throws IOException        Thrown if an I/O error occurs.
     */
    @PostMapping("/resend/confirm-token")
    @Override
    public ResponseEntity<?> resendConfirmEmailToken(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage, @RequestBody TokenConfirmationDTO dto) throws MessagingException, IOException {
        String htmlTemplate = HtmlTemplateUtils.getConfirmationHtmlTemplate(siteLanguage);
        if (mailSenderService.resendEmail(dto.getEmail(), htmlTemplate)) {
            return ResponseEntity.status(HttpStatus.OK).body("Confirm token has been sent one more time");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad input");
    }

}
