package com.booking.app.services;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.exception.exception.EmailAlreadyTakenException;
import jakarta.mail.MessagingException;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

/**
 * Service interface for user security operations.
 */
@Validated
public interface RegistrationService {

    /**
     * Registers a new user based on the provided registration information.
     *
     * @param dto      The RegistrationDTO containing user registration details.
     * @param language The language for the confirmation email template.
     * @return EmailDTO Returns an EmailDTO containing information about the registration confirmation email.
     * @throws EmailAlreadyTakenException If a user with the provided email already exists and is enabled.
     * @throws MessagingException         If there is an issue with sending the confirmation email.
     */
    EmailDTO register(RegistrationDTO dto, String language) throws EmailAlreadyTakenException, MessagingException, IOException;

    /**
     * Enables a user if the provided token confirmation details are valid.
     *
     * @param dto The TokenConfirmationDTO containing token confirmation details.
     * @return boolean Returns true if the user is successfully enabled; otherwise, returns false.
     */
    boolean enableIfValid(TokenConfirmationDTO dto);

}