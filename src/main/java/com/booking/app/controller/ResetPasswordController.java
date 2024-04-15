package com.booking.app.controller;

import com.booking.app.controller.api.ResetPasswordAPI;
import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.services.ResetPasswordService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * ResetPasswordController handles user password reset operations.
 * This controller provides endpoints for sending a password reset token and confirming the reset.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Log4j2
public class ResetPasswordController implements ResetPasswordAPI {

    private final ResetPasswordService resetPasswordService;

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
    public ResponseEntity<?> sendResetToken(@RequestBody EmailDTO dto, @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage) throws MessagingException {
        if (resetPasswordService.hasEmailSent(dto.getEmail(), siteLanguage)) {
            return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Such email doesn't exist in our service");
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
        if (resetPasswordService.resetPassword(dto)) {
            log.info(String.format("User %s has successfully changed its password!", dto.getEmail()));
            return ResponseEntity.status(HttpStatus.OK).body("Password has been successfully changed");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation code is not right");
    }

    /**
     * Handles the request to change a password for an authorized user.
     * <p>
     * This endpoint allows an authenticated user to update their password based on the provided
     * {@link RequestUpdatePasswordDTO}. The authentication is performed using the user's credentials
     * obtained through the {@link AuthenticationPrincipal} annotation.
     *
     * @param updatePasswordDTO The data transfer object containing the new password information.
     * @param userCredentials   The authentication principal representing the current user's credentials.
     * @return A ResponseEntity indicating the success or failure of the password update operation.
     * - If the password is successfully updated, returns HTTP 200 OK with a success message.
     * - If the last password provided is incorrect, returns HTTP 400 Bad Request with an error message.
     */
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody RequestUpdatePasswordDTO updatePasswordDTO, @AuthenticationPrincipal UserCredentials userCredentials) {
        if (resetPasswordService.changePassword(updatePasswordDTO, userCredentials)) {
            return ResponseEntity.ok().body("Password has been successfully updated");
        } else return ResponseEntity.badRequest().body("Last password is not right");
    }

    /**
     * Handles the request to resend the reset password email.
     *
     * @param dto The TokenConfirmationDTO containing the user's email.
     * @return ResponseEntity indicating the result of the resend operation.
     * @throws MessagingException Thrown if an error occurs during email sending.
     * @throws IOException        Thrown if an I/O error occurs.
     */
    @PostMapping("/resend/reset-token")
    public ResponseEntity<?> resendResetPasswordToken(@RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage, @RequestBody TokenConfirmationDTO dto) throws MessagingException, IOException {
        if (resetPasswordService.hasEmailSent(dto.getEmail(), siteLanguage)) {
            return ResponseEntity.status(HttpStatus.OK).body("Reset token has been sent one more time");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad input");
    }

}
