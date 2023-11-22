package com.booking.app.controller;

import com.booking.app.controller.api.RegisterAPI;
import com.booking.app.dto.*;
import com.booking.app.exception.exception.EmailExistsException;
import com.booking.app.exception.exception.UsernameExistsException;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
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
public class RegisterController implements RegisterAPI {

    private final UserSecurityService userSecurityService;
    private final MailSenderService mailSenderService;

    /**
     * Handles user registration request.
     *
     * @param dto The RegistrationDTO containing user registration information.
     * @return ResponseDTO containing information about the registration process.
     * @throws EmailExistsException Thrown if the email already exists.
     * @throws EmailExistsException Thrown if the username already exists.
     * @throws MessagingException   Thrown if an error occurs during email sending.
     * @throws IOException          Thrown if an I/O error occurs.
     */
    @PostMapping("/register")
    @Override
    public ResponseEntity<?> signUp(@RequestBody RegistrationDTO dto) throws EmailExistsException, MessagingException, IOException, UsernameExistsException {
        EmailDTO register = userSecurityService.register(dto);
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
        if (userSecurityService.enableIfValid(dto)) {
            return ResponseEntity.status(HttpStatus.OK).body("User successfully confirmed its email");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not right");
    }

    /**
     * Handles the request to resend the confirmation email.
     *
     * @param dto The TokenConfirmationDTO containing the user's email.
     * @return ResponseEntity indicating the result of the resend operation.
     * @throws MessagingException Thrown if an error occurs during email sending.
     * @throws IOException        Thrown if an I/O error occurs.
     */
    @PostMapping("/resend-confirm-email")
    @Override
    public ResponseEntity<?> resendConfirmEmailToken(@RequestBody TokenConfirmationDTO dto) throws MessagingException, IOException {
        if (mailSenderService.resendEmail(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.OK).body("Confirm password token has been sent one more time");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad input");
    }
}
