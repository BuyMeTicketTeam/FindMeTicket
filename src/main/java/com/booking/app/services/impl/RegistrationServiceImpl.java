package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.CodeConfirmationDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.EmailAlreadyTakenException;
import com.booking.app.exception.exception.InvalidConfirmationCodeException;
import com.booking.app.exception.exception.UserCredentialsNotFoundException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import com.booking.app.services.UserCredentialsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booking.app.constant.MailConstants.EMAIL_CONFIRMATION_SUBJECT;
import static com.booking.app.constant.RegistrationConstantMessages.EMAIL_IS_ALREADY_TAKEN_MESSAGE;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(propagation = Propagation.REQUIRED)
public class RegistrationServiceImpl implements RegistrationService {

    private final UserCredentialsService userCredentialsService;
    private final UserMapper mapper;
    private final MailSenderService mailService;
    private final ConfirmationCodeService confirmationCodeService;

    @Override
    public EmailDTO register(RegistrationDTO dto, String language) throws MessagingException {
        UserCredentials userCredentials = findOrCreateUserCredentials(dto);
        mailService.sendEmail(language, EMAIL_CONFIRMATION_SUBJECT, userCredentials.getUser().getConfirmationCode().getCode(), userCredentials);
        log.info("User with ID: {} has successfully registered.", userCredentials.getId());
        return mapper.toEmailDTO(userCredentials);
    }

    @Override
    public void confirmCode(CodeConfirmationDTO dto) {
        userCredentialsService.findByEmail(dto.getEmail())
                .ifPresentOrElse(userCredentials -> {
                    if (!userCredentials.isEnabled()) {
                        verifyUserCredentials(dto, userCredentials);
                    }
                }, () -> {
                    throw new UserCredentialsNotFoundException();
                });
    }

    /**
     * Verifies the user's credentials using the provided code.
     *
     * @param dto             the data transfer object containing the code and email for confirmation
     * @param userCredentials the user credentials to be verified
     */
    private void verifyUserCredentials(CodeConfirmationDTO dto, UserCredentials userCredentials) {
        if (confirmationCodeService.verifyCode(userCredentials.getUser().getConfirmationCode(), dto.getCode())) {
            userCredentialsService.enableUserCredentials(userCredentials);
            log.info("User with ID: {} has successfully confirmed email.", userCredentials.getId());
        } else {
            throw new InvalidConfirmationCodeException();
        }
    }

    /**
     * Finds an existing user by email or creates a new user if not found.
     *
     * @param dto the data transfer object containing user registration details
     * @return the user credentials of the found or newly created user
     * @throws EmailAlreadyTakenException if a user with the provided email already exists and is enabled
     */
    private UserCredentials findOrCreateUserCredentials(RegistrationDTO dto) {
        return userCredentialsService.findByEmail(dto.getEmail())
                .map(user -> {
                    if (user.isEnabled()) {
                        throw new EmailAlreadyTakenException(EMAIL_IS_ALREADY_TAKEN_MESSAGE);
                    } else {
                        return userCredentialsService.updateUserCredentials(user, dto);
                    }
                })
                .orElseGet(() -> userCredentialsService.createUserCredentials(dto));
    }

}
