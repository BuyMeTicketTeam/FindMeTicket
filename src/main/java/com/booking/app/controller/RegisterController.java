package com.booking.app.controller;

import com.booking.app.controller.api.RegisterAPI;
import com.booking.app.dto.*;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping
@AllArgsConstructor
public class RegisterController implements RegisterAPI {

    private final UserSecurityService service;
    private final MailSenderService mailSenderService;

    @PostMapping("/register")
    @Override
    public ResponseDTO<EmailDTO> signUp(@RequestBody RegistrationDTO dto) throws UserAlreadyExistAuthenticationException, MessagingException, IOException {
        return new ResponseDTO<>(service.register(dto));
    }

    //@ResponseStatus(code = HttpStatus.OK)
    @PostMapping("/confirm-email")
    @Override
    public ResponseEntity<?> confirmEmailToken(@RequestBody TokenConfirmationDTO dto) {
        boolean correctToken = service.enableIfValid(dto);

        if (correctToken) {
            return ResponseEntity.status(HttpStatus.OK).body("User successfully confirmed its email");
        }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not right");
    }

    @PostMapping("resend-confirm-email")
    @Override
    public ResponseEntity<?> resendConfirmEmailToken(@RequestBody TokenConfirmationDTO dto) throws MessagingException, IOException {
        mailSenderService.resendEmail(dto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Confirm password token has been sent one more time");
    }
}
