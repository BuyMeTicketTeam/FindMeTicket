package com.booking.app.services;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import jakarta.mail.MessagingException;
import org.springframework.validation.annotation.Validated;
import java.io.IOException;
import java.util.Optional;

/**
 * Service interface for user security operations.
 */
@Validated
public interface UserSecurityService {

    /**
     * Registers a new user based on the provided registration information.
     *
     * @param securityDTO The RegistrationDTO containing user registration details.
     * @return EmailDTO Returns an EmailDTO containing information about the registration confirmation email.
     * @throws UserAlreadyExistAuthenticationException If a user with the provided email already exists.
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    EmailDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException, MessagingException, IOException;

    /**
     * Finds a user security entity by the provided email address.
     *
     * @param email The email address of the user.
     * @return Optional<UserSecurity> Returns an Optional containing the UserSecurity entity if found, otherwise empty.
     */
    Optional<UserSecurity> findByEmail(String email);

    /**
     * Enables a user if the provided token confirmation details are valid.
     *
     * @param dto The TokenConfirmationDTO containing token confirmation details.
     * @return boolean Returns true if the user is successfully enabled; otherwise, returns false.
     */
    boolean enableIfValid(TokenConfirmationDTO dto);
}