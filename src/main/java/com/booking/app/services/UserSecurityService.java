package com.booking.app.services;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.EmailExistsException;
import com.booking.app.exception.exception.UsernameExistsException;
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
     * @throws EmailExistsException If a user with the provided email already exists.
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    EmailDTO register(RegistrationDTO securityDTO) throws EmailExistsException, MessagingException, IOException, UsernameExistsException;

    /**
     * Enables a user if the provided token confirmation details are valid.
     *
     * @param dto The TokenConfirmationDTO containing token confirmation details.
     * @return boolean Returns true if the user is successfully enabled; otherwise, returns false.
     */
    boolean enableIfValid(TokenConfirmationDTO dto);
}