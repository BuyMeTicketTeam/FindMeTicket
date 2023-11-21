package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.enums.EnumRole;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.TokenService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Service class for user security operations.
 */
@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final MailSenderService mailService;
    private final TokenService tokenService;
  //  private final UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSecurity userSecurity = userSecurityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
        return userSecurity;
    }


    /**
     * Registers a new user based on the provided registration information.
     *
     * @param securityDTO The RegistrationDTO containing user registration details.
     * @return EmailDTO Returns an EmailDTO containing information about the registration confirmation email.
     * @throws UserAlreadyExistAuthenticationException If a user with the provided email already exists.
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    @Override
    public EmailDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException, MessagingException{
        Optional<UserSecurity> byEmail = userSecurityRepository.findByEmail(securityDTO.getEmail());

        if (byEmail.isPresent() && byEmail.get().isEnabled()) {
            throw new UserAlreadyExistAuthenticationException("We’re sorry. This email already exists");
        }
        if (byEmail.isPresent()) {
            deleteUserIfNotConfirmed(byEmail.get());
        }

        return performRegistration(securityDTO);
    }

    /**
     * Deletes user
     *
     * @param byEmail UserSecurity that must be deleted
     */
    @Transactional
    public void deleteUserIfNotConfirmed(UserSecurity byEmail){
        verifyEmailRepository.deleteById(byEmail.getUser().getConfirmToken().getId());
    }

    /**
     * Performs registration:
     *  Creates user, token
     *  Sends email
     *
     * @param securityDTO The RegistrationDTO containing user registration details.
     * @return EmailDTO containing email
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    //    @Transactional
    public EmailDTO performRegistration(RegistrationDTO securityDTO) throws MessagingException{
        UserSecurity securityEntity = mapper.toEntityRegistration(securityDTO);
        securityEntity.setPassword(passwordEncoder.encode(securityDTO.getPassword()));

        User user = createNewRegisteredUser(securityEntity);
        user.setSecurity(securityEntity);
        user.getSecurity().setUser(user);

        mailService.sendEmail("confirmMail", "Email confirmation", user.getConfirmToken().getToken(), securityEntity);


        return mapper.toEmail(securityEntity);
    }

    /**
     * Generates a token, saves user to the Database
     *
     * @param userSecurity UserSecurity that must be saved
     * @return User that was saved
     */
    @Transactional
    public User createNewRegisteredUser(UserSecurity userSecurity) {
        Role role = roleRepository.findByEnumRole(EnumRole.USER);
        User user = User.builder()
                .registrationDate(LocalDate.now())
                .security(userSecurity)
                .role(role)
                .build();

        ConfirmToken confirmToken = tokenService.createConfirmToken(user);

        verifyEmailRepository.save(confirmToken);
        user.setConfirmToken(confirmToken);


        return user;
    }

    /**
     * Finds a user security entity by the provided email address.
     *
     * @param email The email address of the user.
     * @return Optional<UserSecurity> Returns an Optional containing the UserSecurity entity if found, otherwise empty.
     */
    @Override
    public Optional<UserSecurity> findByEmail(String email) {
        return userSecurityRepository.findByEmail(email);
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
        Optional<UserSecurity> userByEmail = userSecurityRepository.findByEmail(dto.getEmail());

        if (!userByEmail.get().isEnabled() && tokenService.verifyToken(dto.getEmail(), dto.getToken())) {
            userSecurityRepository.enableAllBooleansForUser(userByEmail.get().getId());
            return true;

        }
        return false;
    }

}
