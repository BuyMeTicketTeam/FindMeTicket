package com.booking.app.services.impl;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.ConfirmationCode;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.PasswordIsNotRightException;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.PasswordService;
import com.booking.app.services.UserCredentialsService;
import com.booking.app.util.HtmlTemplateUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booking.app.constant.MailConstants.RESET_PASSWORD_SUBJECT;


@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class PasswordServiceImpl implements PasswordService {

    private final MailSenderService mailSenderService;
    private final UserCredentialsService userCredentialsService;
    private final ConfirmationCodeService confirmationCodeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendResetCode(String email, String language) throws MessagingException {
        UserCredentials userCredentials = userCredentialsService.findByEmail(email);
        if (userCredentialsService.isEnabled(userCredentials)) {
            User user = userCredentials.getUser();
            ConfirmationCode newCode = ConfirmationCode.create(user);

            sendMail(language, newCode, userCredentials);
            confirmationCodeService.save(newCode);
        }
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        UserCredentials userCredentials = userCredentialsService.findByEmail(dto.getEmail());
        if (userCredentialsService.isEnabled(userCredentials)
                && confirmationCodeService.verifyCode(userCredentials.getUser().getConfirmationCode(), dto.getCode())) {
            userCredentialsService.updatePassword(userCredentials.getId(), passwordEncoder.encode(dto.getPassword()));
        }
    }

    @Override
    public void changePassword(RequestUpdatePasswordDTO dto, UserCredentials userCredentials) {
        String currentPassword = userCredentials.getPassword();
        String lastPassword = dto.getLastPassword();
        if (passwordEncoder.matches(lastPassword, currentPassword)) {
            userCredentialsService.updatePassword(userCredentials.getId(), passwordEncoder.encode(dto.getPassword()));
        }
        throw new PasswordIsNotRightException();
    }

    /**
     * Sends an email with the reset password token.
     *
     * @param language        the language preference for the email content
     * @param newCode         the confirmation token to be included in the email
     * @param userCredentials the user's credentials
     * @throws MessagingException if there is an error while sending the email
     */
    private void sendMail(String language, ConfirmationCode newCode, UserCredentials userCredentials) throws MessagingException {
        String htmlTemplate = HtmlTemplateUtils.getResetPasswordHtmlTemplate(language);
        mailSenderService.sendEmail(htmlTemplate, RESET_PASSWORD_SUBJECT, newCode.getCode(), userCredentials);
    }

}
