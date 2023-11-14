package com.booking.app.controller;

import com.booking.app.controller.api.ResetPasswordAPI;
import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;

@RestController
@RequestMapping
@AllArgsConstructor
public class ResetPasswordController implements ResetPasswordAPI {
    private final ResetPasswordService service;

    @PostMapping("/reset")
    @Override
    public ResponseEntity<?> sendResetToken(@RequestBody EmailDTO dto) throws MessagingException, IOException {
        if( service.sendEmailResetPassword(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Such email doesn't exist in out service");
    }

    @PostMapping("/new-password")
    @Override
    public ResponseEntity<?> confirmResetPassword(@RequestBody ResetPasswordDTO dto) {
        if(service.resetPassword(dto)){
            return ResponseEntity.status(HttpStatus.OK).body("Password has been successfully changed");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation code is not right");
    }
}
