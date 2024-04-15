package com.booking.app.services.impl;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.ResourceNotFoundException;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import com.booking.app.services.TokenService;
import com.booking.app.util.HtmlTemplateUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for resetting passwords.
 */
@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final MailSenderService mailSenderService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Generates a new token for resetting the password and sends an email to the specified recipient.
     *
     * @param email The email address of the recipient.
     * @return Returns true if the email with the reset password link was sent successfully; otherwise, returns false.
     * @throws MessagingException If there is an issue with sending the email.
     */
    @Transactional
    public boolean hasEmailSent(String email, String language) throws MessagingException {
        UserCredentials userCredentials = userCredentialsRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No such email"));

        if (!userCredentials.isEnabled()) {
            return false;
        } else {
            User user = userCredentials.getUser();
            verifyEmailRepository.delete(user.getConfirmToken());

            ConfirmToken confirmToken = ConfirmToken.createConfirmToken();
            confirmToken.setUser(user);
            user.setConfirmToken(confirmToken);

            verifyEmailRepository.save(confirmToken);
            String htmlTemplate = HtmlTemplateUtils.getResetPasswordHtmlTemplate(language);
            mailSenderService.sendEmail(htmlTemplate, "Reset password", confirmToken.getToken(), userCredentials);
            return true;
        }

    }

    /**
     * Resets the password to a new one using the information provided in the ResetPasswordDTO.
     *
     * @param dto The ResetPasswordDTO containing the email and the new password.
     * @return Returns true if the password was successfully changed; otherwise, returns false.
     */
    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordDTO dto) {
        UserCredentials userCredentials
                = userCredentialsRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("No such email"));

        if (!userCredentials.isEnabled()) {
            return false;
        } else {
            if (!tokenService.verifyToken(dto.getEmail(), dto.getToken())) return false;

            userCredentials.setPassword(passwordEncoder.encode(dto.getPassword()));
            userCredentialsRepository.save(userCredentials);
            return true;
        }
    }

    /**
     * Changes the password for the specified user based on the provided update information.
     * <p>
     * This method checks if the provided last password matches the current password of the user.
     * If the match is successful, the user's password is updated with the new password, and the
     * method returns true. If the last password does not match, the password remains unchanged,
     * and the method returns false.
     *
     * @param updatePasswordDTO The data transfer object containing the last and new password information.
     * @param userCredentials   The credentials of the user for whom the password is to be changed.
     * @return {@code true} if the password is successfully changed, {@code false} otherwise.
     */
    @Override
    public boolean changePassword(RequestUpdatePasswordDTO updatePasswordDTO, UserCredentials userCredentials) {
        String currentPassword = userCredentials.getPassword();
        String lastPassword = updatePasswordDTO.getLastPassword();

        if (passwordEncoder.matches(lastPassword, currentPassword)) {
            String newPassword = updatePasswordDTO.getPassword();
            userCredentials.setPassword(passwordEncoder.encode(newPassword));
            userCredentialsRepository.save(userCredentials);
            return true;
        }
        return false;
    }

}
