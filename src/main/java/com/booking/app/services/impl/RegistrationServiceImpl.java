package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.enums.EnumProvider;
import com.booking.app.enums.EnumRole;
import com.booking.app.exception.exception.EmailAlreadyTakenException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import com.booking.app.services.TokenService;
import com.booking.app.util.AvatarGenerator;
import com.booking.app.util.HtmlTemplateUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.booking.app.constant.MailConstants.EMAIL_CONFIRMATION_SUBJECT;
import static com.booking.app.constant.RegistrationConstantMessages.EMAIL_IS_ALREADY_TAKEN_MESSAGE;

/**
 * Service class for user registration operations.
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {


    private final UserCredentialsRepository userCredentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final MailSenderService mailService;
    private final TokenService tokenService;

    @Override
    @Transactional
    public EmailDTO register(RegistrationDTO dto, String language) throws MessagingException {
        UserCredentials userCredentials = findUser(dto);
        sendMail(language, userCredentials);
        return mapper.toEmailDTO(userCredentials);
    }

    @Override
    @Transactional
    public boolean enableIfValid(TokenConfirmationDTO dto) {
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByEmail(dto.getEmail());
        return userCredentials.map(user -> {
            if (!user.isEnabled() && tokenService.verifyToken(dto.getEmail(), dto.getToken())) {
                userCredentialsRepository.enableUser(user.getId());
                return true;
            } else {
                return false;
            }
        }).orElse(false);
    }

    /**
     * Sends a confirmation email to the user.
     *
     * @param language        The language for the email template.
     * @param userCredentials The UserCredentials of the user to whom the email is sent.
     * @throws MessagingException If there is an issue with sending the email.
     */
    private void sendMail(String language, UserCredentials userCredentials) throws MessagingException {
        String template = HtmlTemplateUtils.getConfirmationHtmlTemplate(language);
        mailService.sendEmail(template, EMAIL_CONFIRMATION_SUBJECT, userCredentials.getUser().getConfirmToken().getToken(), userCredentials);
    }

    /**
     * Finds an existing user by email or creates a new user if not found.
     *
     * @param dto The RegistrationDTO containing user registration details.
     * @return UserCredentials Returns the UserCredentials of the found or newly created user.
     * @throws EmailAlreadyTakenException If a user with the provided email already exists and is enabled.
     */
    private UserCredentials findUser(RegistrationDTO dto) {
        return userCredentialsRepository.findByEmail(dto.getEmail())
                .map(user -> {
                    if (user.isEnabled()) {
                        throw new EmailAlreadyTakenException(EMAIL_IS_ALREADY_TAKEN_MESSAGE);
                    }
                    return updateUser(user, dto);
                })
                .orElseGet(() -> createNewUser(dto));
    }


    /**
     * Updates the details of an existing user.
     *
     * @param userCredentials The existing UserCredentials to be updated.
     * @param dto             The RegistrationDTO containing new user details.
     * @return UserCredentials Returns the updated UserCredentials.
     */
    private UserCredentials updateUser(UserCredentials userCredentials, RegistrationDTO dto) {
        userCredentials.setPassword(passwordEncoder.encode(dto.getPassword()));
        ConfirmToken confirmToken = ConfirmToken.createConfirmToken();
        User user = userCredentials.getUser();
        user.setConfirmToken(confirmToken);
        user.setNotification(dto.getNotification());
        userCredentialsRepository.save(userCredentials);
        return userCredentials;
    }

    /**
     * Creates a new user with the provided registration details.
     *
     * @param dto The RegistrationDTO containing user registration details.
     * @return UserCredentials Returns the newly created UserCredentials.
     */
    @NotNull
    private UserCredentials createNewUser(RegistrationDTO dto) {
        UserCredentials userCredentials = mapper.toUserSecurity(dto);
        userCredentials.setProvider(EnumProvider.LOCAL);
        userCredentials.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role = roleRepository.findRoleByEnumRole(EnumRole.USER);
        ConfirmToken confirmToken = ConfirmToken.createConfirmToken();
        byte[] avatarAsBytes = AvatarGenerator.createRandomAvatarAsBytes();

        User user = User.createUser(userCredentials, role, confirmToken, dto.getNotification(), avatarAsBytes);
        userCredentials.setUser(user);
        userCredentialsRepository.save(userCredentials);
        return userCredentials;
    }

}
