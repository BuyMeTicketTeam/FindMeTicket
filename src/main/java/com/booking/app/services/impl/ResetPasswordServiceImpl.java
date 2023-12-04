package com.booking.app.services.impl;

import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import com.booking.app.services.TokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Optional;

/**
 * Service class for resetting passwords.
 */
@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final MailSenderService mailSenderService;
    private final UserSecurityRepository userSecurityRepository;
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
    @Override
    @Transactional
    public boolean sendEmailResetPassword(String email) throws MessagingException {

        Optional<UserSecurity> userFromDb = userSecurityRepository.findByEmail(email);

        if (!userFromDb.isPresent() || !userFromDb.get().isEnabled()) {
            return false;
        } else {
            UserSecurity userSecurity = userFromDb.orElseThrow(() -> new UsernameNotFoundException("No such email"));
            User user = userSecurity.getUser();

            verifyEmailRepository.delete(user.getConfirmToken());

            ConfirmToken confirmToken = tokenService.createConfirmToken(user);
            user.setConfirmToken(confirmToken);

            verifyEmailRepository.save(confirmToken);
            mailSenderService.sendEmail("resetPasswordUa", "Reset password", confirmToken.getToken(), userFromDb.get());
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

        Optional<UserSecurity> userFromDb = userSecurityRepository.findByEmail(dto.getEmail());

        if (!userFromDb.isPresent() || !userFromDb.get().isEnabled()) {
            return false;
        } else {
            UserSecurity userSecurity = userFromDb.orElseThrow(() -> new UsernameNotFoundException("No such email"));
            if (!tokenService.verifyToken(dto.getEmail(), dto.getToken())) return false;

            userSecurity.setPassword(passwordEncoder.encode(dto.getPassword()));
            userSecurityRepository.save(userSecurity);
            return true;
        }
    }

}
