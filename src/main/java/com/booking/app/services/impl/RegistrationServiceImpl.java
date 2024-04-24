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
import com.booking.app.exception.exception.EmailAlreadyExistsException;
import com.booking.app.exception.exception.UsernameAlreadyExistsException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import com.booking.app.services.TokenService;
import com.booking.app.util.AvatarGenerator;
import com.booking.app.util.HtmlTemplateUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for user registration operations.
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class RegistrationServiceImpl implements RegistrationService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final RoleRepository roleRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final MailSenderService mailService;
    private final TokenService tokenService;

    /**
     * Registers a new user based on the provided registration information.
     *
     * @param registrationDTO The RegistrationDTO containing user registration details.
     * @return EmailDTO Returns an EmailDTO containing information about the registration confirmation email.
     * @throws EmailAlreadyExistsException    If a user with the provided email already exists.
     * @throws UsernameAlreadyExistsException If a user with the provided username already exists.
     * @throws MessagingException             If there is an issue with sending the confirmation email.
     */
    @Override
    public EmailDTO register(RegistrationDTO registrationDTO, String language) throws MessagingException {
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername());
        handleUserPresence(registrationDTO, userCredentials);

        return performRegistration(registrationDTO, language);
    }

    /**
     * If a user with the same email address exists and is enabled, it throws an EmailAlreadyExistsException.
     * If a user with the same email address exists but is not enabled, it deletes the user from the repository.
     *
     * @param registrationDTO The RegistrationDTO containing user registration details.
     * @param userCredentials Optional containing the user credentials, if available.
     * @throws EmailAlreadyExistsException If a user with the same email address already exists and is enabled.
     */
    private void handleUserPresence(RegistrationDTO registrationDTO, Optional<UserCredentials> userCredentials) {
        if (userCredentials.isPresent()
                && userCredentials.get().getEmail().equals(registrationDTO.getEmail())
                && userCredentials.get().isEnabled()) {
            throw new EmailAlreadyExistsException("We’re sorry. This email already exists");
        } else {
            userCredentials.ifPresent(user -> userCredentialsRepository.deleteById(user.getId()));
        }
    }

    /**
     * Creates UserCredentials, saves new user into Database and sends Email confirmation
     *
     * @param registrationDTO The RegistrationDTO containing user registration details.
     * @return EmailDTO containing email
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    private EmailDTO performRegistration(RegistrationDTO registrationDTO, String language) throws MessagingException {
        UserCredentials userCredentials = mapper.toUserSecurity(registrationDTO);
        userCredentials.setProvider(EnumProvider.LOCAL);
        userCredentials.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        saveUser(userCredentials, registrationDTO.getNotification());

        String template = HtmlTemplateUtils.getConfirmationHtmlTemplate(language);
        mailService.sendEmail(template, "Email confirmation", userCredentials.getUser().getConfirmToken().getToken(), userCredentials);

        return mapper.toEmailDTO(userCredentials);
    }

    /**
     * Generates a token, saves user to the Database
     *
     * @param userCredentials UserSecurity that must be saved
     */
    private void saveUser(UserCredentials userCredentials, Boolean notification) {
        Role role = roleRepository.findRoleByEnumRole(EnumRole.USER);
        ConfirmToken confirmToken = ConfirmToken.createConfirmToken();
        byte[] avatarAsBytes = AvatarGenerator.createRandomAvatarAsBytes();

        User user = User.createUser(userCredentials, role, confirmToken, notification, avatarAsBytes);
        userCredentials.setUser(user);
        userCredentialsRepository.save(userCredentials);
    }

    /**
     * Enables a user if the provided token confirmation details are valid.
     *
     * @param dto The TokenConfirmationDTO containing token confirmation details.
     * @return boolean Returns true if the user is successfully enabled; otherwise, returns false.
     */
    @Override
    public boolean enableIfValid(TokenConfirmationDTO dto) {
        Optional<UserCredentials> userByEmail = userCredentialsRepository.findByEmail(dto.getEmail());

        if (userByEmail.isPresent() && !userByEmail.get().isEnabled() && tokenService.verifyToken(dto.getEmail(), dto.getToken())) {
            userCredentialsRepository.enableUser(userByEmail.get().getId());
            return true;

        }
        return false;
    }

}
