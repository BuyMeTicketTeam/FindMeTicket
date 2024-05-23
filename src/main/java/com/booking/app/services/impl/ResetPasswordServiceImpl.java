package com.booking.app.services.impl;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.UserNotFoundException;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import com.booking.app.services.TokenService;
import com.booking.app.util.HtmlTemplateUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.booking.app.constant.MailConstants.RESET_PASSWORD_SUBJECT;
import static com.booking.app.constant.ResetPasswordConstantMessages.THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED;

/**
 * Service class of management over user's password
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final MailSenderService mailSenderService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean sendCode(String email, String language) throws MessagingException {
        UserCredentials userCredentials = checkUserEnablement(email);
        User user = userCredentials.getUser();
        ConfirmToken newCode = createNewCode(user);
        verifyEmailRepository.save(newCode);
        sendMail(language, newCode, userCredentials);
        return true;
    }

    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordDTO dto) {
        UserCredentials userCredentials = checkUserEnablement(dto.getEmail());
        if (isCodeValid(dto)) {
            updatePassword(dto, userCredentials);
            return true;
        }
        return false;
    }

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

    /**
     * Sends an email with the reset password token.
     *
     * @param language        the language preference for the email content
     * @param newCode         the confirmation token to be included in the email
     * @param userCredentials the user's credentials
     * @throws MessagingException if there is an error while sending the email
     */
    private void sendMail(String language, ConfirmToken newCode, UserCredentials userCredentials) throws MessagingException {
        String htmlTemplate = HtmlTemplateUtils.getResetPasswordHtmlTemplate(language);
        mailSenderService.sendEmail(htmlTemplate, RESET_PASSWORD_SUBJECT, newCode.getToken(), userCredentials);
    }

    /**
     * Creates a new confirmation token for the specified user.
     *
     * @param user the user for whom the token is created
     * @return the newly created confirmation token
     */
    private static ConfirmToken createNewCode(User user) {
        ConfirmToken newCode = ConfirmToken.createConfirmToken();
        newCode.setUser(user);
        user.setConfirmToken(newCode);
        return newCode;
    }

    /**
     * Retrieves UserCredentials by email.
     *
     * @param email the email of the user
     * @return the UserCredentials of the user
     * @throws UserNotFoundException if no user is found with the specified email
     */
    private UserCredentials checkUserEnablement(String email) {
        return userCredentialsRepository.findByEmail(email)
                .filter(UserCredentials::isEnabled)
                .orElseThrow(() -> new UserNotFoundException(THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED));
    }

    /**
     * Updates the password for the specified user.
     *
     * @param dto             the data transfer object containing the new password
     * @param userCredentials the credentials of the user
     */
    private void updatePassword(ResetPasswordDTO dto, UserCredentials userCredentials) {
        userCredentials.setPassword(passwordEncoder.encode(dto.getPassword()));
        userCredentialsRepository.save(userCredentials);
        log.info("User {} has successfully reset their password!", dto.getEmail());
    }

    /**
     * Validates the confirmation code provided in the ResetPasswordDTO.
     *
     * @param dto the data transfer object containing the confirmation code
     * @return true if the confirmation code is valid, false otherwise
     */
    private boolean isCodeValid(ResetPasswordDTO dto) {
        return tokenService.verifyToken(dto.getEmail(), dto.getToken());
    }

}
