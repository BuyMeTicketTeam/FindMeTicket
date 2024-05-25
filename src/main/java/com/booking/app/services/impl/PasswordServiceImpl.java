package com.booking.app.services.impl;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.PasswordDto;
import com.booking.app.entity.ConfirmationCode;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.LastPasswordIsNotRightException;
import com.booking.app.exception.exception.UserCredentialsNotFoundException;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.PasswordService;
import com.booking.app.services.UserCredentialsService;
import com.booking.app.util.HtmlTemplateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booking.app.constant.MailConstants.RESET_PASSWORD_SUBJECT;

/**
 * Implementation of {@link PasswordService} for managing password-related operations.
 * This service handles sending reset codes, resetting passwords, and changing passwords.
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class PasswordServiceImpl implements PasswordService {

    private final MailSenderService mailSenderService;
    private final UserCredentialsService userCredentialsService;
    private final ConfirmationCodeService confirmationCodeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendResetCode(String email, String language) {
        userCredentialsService.findByEmail(email)
                .ifPresentOrElse(userCredentials -> {
                            if (userCredentialsService.isEnabled(userCredentials)) {
                                User user = userCredentials.getUser();
                                ConfirmationCode newCode = ConfirmationCode.createCode();
                                user.setConfirmationCode(newCode);

                                sendMail(language, newCode, userCredentials);
                                confirmationCodeService.save(newCode);
                            }
                        },
                        () -> {
                            throw new UserCredentialsNotFoundException();
                        });

    }

    @Override
    public void resetPassword(PasswordDto dto) {
        userCredentialsService.findByEmail(dto.getEmail())
                .ifPresentOrElse(userCredentials -> {
                    if (userCredentialsService.isEnabled(userCredentials)
                            && confirmationCodeService.verifyCode(userCredentials.getUser().getConfirmationCode(), dto.getCode())) {
                        userCredentialsService.updatePassword(userCredentials.getId(), passwordEncoder.encode(dto.getPassword()));
                    }
                }, () -> {
                    throw new UserCredentialsNotFoundException();
                });

    }

    @Override
    public void changePassword(RequestUpdatePasswordDTO dto, UserCredentials userCredentials) {
        String currentPassword = userCredentials.getPassword();
        String lastPassword = dto.getLastPassword();
        if (passwordEncoder.matches(lastPassword, currentPassword)) {
            userCredentialsService.updatePassword(userCredentials.getId(), passwordEncoder.encode(dto.getPassword()));
        } else {
            throw new LastPasswordIsNotRightException();
        }
    }

    /**
     * Sends an email with the reset password token.
     *
     * @param language        the language preference for the email content
     * @param newCode         the confirmation token to be included in the email
     * @param userCredentials the user's credentials
     */
    private void sendMail(String language, ConfirmationCode newCode, UserCredentials userCredentials) {
        String htmlTemplate = HtmlTemplateUtils.getResetPasswordHtmlTemplate(language);
        mailSenderService.sendEmail(htmlTemplate, RESET_PASSWORD_SUBJECT, newCode.getCode(), userCredentials);
    }

}
