package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.*;
import com.booking.app.enums.EnumProvider;
import com.booking.app.enums.EnumRole;
import com.booking.app.exception.exception.EmailExistsException;
import com.booking.app.exception.exception.UsernameExistsException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.TokenService;
import com.booking.app.services.RegistrationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Service class for user registration operations.
 */
@Service
@RequiredArgsConstructor
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
     * @throws EmailExistsException    If a user with the provided email already exists.
     * @throws UsernameExistsException If a user with the provided username already exists.
     * @throws MessagingException      If there is an issue with sending the confirmation email.
     */
    @Override
    public EmailDTO register(RegistrationDTO registrationDTO) throws EmailExistsException, MessagingException, UsernameExistsException {
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername());
        if (userCredentials.isPresent()
                && userCredentials.get().getEmail().equals(registrationDTO.getEmail())
                && userCredentials.get().isEnabled()) {

            throw new EmailExistsException("We’re sorry. This email already exists");
        }

        if (userCredentials.isPresent()
                && userCredentials.get().getUsername().equals(registrationDTO.getUsername())
                && userCredentials.get().isEnabled()) {
            throw new UsernameExistsException("We’re sorry. This username already exists");
        }
        if (userCredentials.isPresent()) {
            deleteUserIfNotConfirmed(userCredentials.get());
        }

        return performRegistration(registrationDTO);
    }

    /**
     * Deletes user
     *
     * @param byEmail UserCredentials that must be deleted
     */
    @Transactional
    public void deleteUserIfNotConfirmed(UserCredentials byEmail) {
        verifyEmailRepository.deleteById(byEmail.getUser().getConfirmToken().getId());
    }

    /**
     * Performs registration:
     * Creates user, token
     * Sends email
     *
     * @param securityDTO The RegistrationDTO containing user registration details.
     * @return EmailDTO containing email
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    //    @Transactional
    public EmailDTO performRegistration(RegistrationDTO securityDTO) throws MessagingException {
        UserCredentials securityEntity = mapper.toUserSecurity(securityDTO);
        securityEntity.setProvider(EnumProvider.LOCAL);
        securityEntity.setPassword(passwordEncoder.encode(securityDTO.getPassword()));

        User user = createNewRegisteredUser(securityEntity);

        mailService.sendEmail("confirmMailUa", "Email confirmation", user.getConfirmToken().getToken(), securityEntity);

        return mapper.toEmailDTO(securityEntity);
    }

    /**
     * Generates a token, saves user to the Database
     *
     * @param userCredentials UserSecurity that must be saved
     * @return User that was saved
     */
    @Transactional
    public User createNewRegisteredUser(UserCredentials userCredentials) {
        Role role = roleRepository.findRoleByEnumRole(EnumRole.USER);

        User user = User.builder()
                .security(userCredentials)
                .role(role)
                .build();

        ConfirmToken confirmToken = tokenService.createConfirmToken(user);

        userCredentials.setUser(user);
        user.setConfirmToken(confirmToken);

        userCredentialsRepository.save(userCredentials);

        return user;
    }

    /**
     * Enables a user if the provided token confirmation details are valid.
     *
     * @param dto The TokenConfirmationDTO containing token confirmation details.
     * @return boolean Returns true if the user is successfully enabled; otherwise, returns false.
     */
    @Transactional
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