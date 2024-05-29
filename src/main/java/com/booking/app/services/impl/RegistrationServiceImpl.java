package com.booking.app.services.impl;

import com.booking.app.dto.CodeConfirmationDto;
import com.booking.app.dto.EmailDto;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.User;
import com.booking.app.exception.exception.EmailAlreadyTakenException;
import com.booking.app.exception.exception.InvalidConfirmationCodeException;
import com.booking.app.exception.exception.UserNotFoundException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import com.booking.app.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booking.app.constant.RegistrationConstantMessages.EMAIL_IS_ALREADY_TAKEN_MESSAGE;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(propagation = Propagation.REQUIRED)
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final UserMapper mapper;
    private final MailSenderService mailService;
    private final ConfirmationCodeService confirmationCodeService;

    @Override
    @Transactional
    public EmailDto register(RegistrationDTO dto, String language) throws MessagingException {
        User user = findOrCreateUser(dto);
        mailService.sendVerificationCode(user.getEmail(), language);
        log.info("User with ID: {} has successfully registered.", user.getId());
        return mapper.toEmailDto(user);
    }

    @Override
    public void confirmCode(CodeConfirmationDto dto) {
        userService.findByEmail(dto.getEmail())
                .ifPresentOrElse(user -> {
                    if (!user.isEnabled()) {
                        verifyUser(dto, user);
                    }
                }, () -> {
                    throw new UserNotFoundException();
                });
    }

    /**
     * Verifies the user using the provided code.
     *
     * @param dto  the data transfer object containing the code and email for confirmation
     * @param user the user to be verified
     */
    private void verifyUser(CodeConfirmationDto dto, User user) {
        if (confirmationCodeService.verifyCode(user.getConfirmationCode(), dto.getCode())) {
            userService.enableUser(user);
            log.info("User with ID: {} has successfully confirmed email.", user.getId());
        } else {
            throw new InvalidConfirmationCodeException();
        }
    }

    /**
     * Finds an existing user by email or creates a new user if not found.
     *
     * @param dto the data transfer object containing user registration details
     * @return the user  of the found or newly created user
     * @throws EmailAlreadyTakenException if a user with the provided email already exists and is enabled
     */
    private User findOrCreateUser(RegistrationDTO dto) {
        return userService.findByEmail(dto.getEmail())
                .map(user -> {
                    if (user.isEnabled()) {
                        throw new EmailAlreadyTakenException(EMAIL_IS_ALREADY_TAKEN_MESSAGE);
                    } else {
                        return userService.updateUser(user, dto);
                    }
                })
                .orElseGet(() -> userService.saveUser(dto));
    }

}
