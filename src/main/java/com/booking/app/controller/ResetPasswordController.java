package com.booking.app.controller;

import com.booking.app.controller.api.ResetPasswordAPI;
import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import com.booking.app.services.UserSecurityService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping
@AllArgsConstructor
public class ResetPasswordController implements ResetPasswordAPI {
    private final ResetPasswordService service;

    @PostMapping("/reset")
    @Override
    public ResponseEntity<?> sendResetToken(EmailDTO dto) {
        boolean correctToken = service.sendEmailResetPassword(dto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent");
    }

    @PostMapping("/new-password")
    @Override
    public ResponseEntity<?> confirmResetPassword(ResetPasswordDTO dto) {
        return null;
    }
}
