package com.booking.app.controller;

import com.booking.app.controller.api.ResetPasswordAPI;
import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.services.ResetPasswordService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

/**
 * ResetPasswordController handles user password reset operations.
 * This controller provides endpoints for sending a password reset token and confirming the reset.
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class ResetPasswordController implements ResetPasswordAPI {

    private final ResetPasswordService service;

    /**
     * Handles the request to send a password reset token to the user's email.
     *
     * @param dto The EmailDTO containing the user's email.
     * @return ResponseEntity indicating the result of the reset token sending operation.
     * @throws MessagingException Thrown if an error occurs during email sending.
     * @throws IOException        Thrown if an I/O error occurs.
     */
    @PostMapping("/reset")
    @Override
    public ResponseEntity<?> sendResetToken(@RequestBody EmailDTO dto) throws MessagingException, IOException {
        if( service.sendEmailResetPassword(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent");
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Such email doesn't exist in out service");
    }

    /**
     * Handles the request to confirm and reset the user's password.
     *
     * @param dto The ResetPasswordDTO containing the reset confirmation information.
     * @return ResponseEntity indicating the result of the password reset operation.
     */
    @PostMapping("/new-password")
    @Override
    public ResponseEntity<?> confirmResetPassword(@RequestBody ResetPasswordDTO dto) {
        if(service.resetPassword(dto)){
            return ResponseEntity.status(HttpStatus.OK).body("Password has been successfully changed");
        }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation code is not right");
    }
}
