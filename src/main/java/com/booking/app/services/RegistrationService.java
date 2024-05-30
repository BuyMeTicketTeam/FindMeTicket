package com.booking.app.services;

import com.booking.app.dto.EmailDto;
import com.booking.app.dto.RegistrationDTO;
import jakarta.mail.MessagingException;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for user security operations.
 */
@Validated
public interface RegistrationService {

    /**
     * Registers a new user and send confirmation code to email.
     *
     * @param dto      the data transfer object containing user registration details
     * @param language the language for the email template
     * @return an EmailDTO containing the email information of the registered user
     * @throws MessagingException if there is an error sending the confirmation email
     */
    EmailDto register(RegistrationDTO dto, String language) throws MessagingException;

}