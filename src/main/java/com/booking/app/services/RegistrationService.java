package com.booking.app.services;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.CodeConfirmationDTO;
import jakarta.mail.MessagingException;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for user security operations.
 */
@Validated
public interface RegistrationService {

    /**
     * Registers a new user.
     *
     * @param dto      the data transfer object containing user registration details
     * @param language the language for the email template
     * @return an EmailDTO containing the email information of the registered user
     * @throws MessagingException if there is an error sending the confirmation email
     */
    EmailDTO register(RegistrationDTO dto, String language) throws MessagingException;

    /**
     * Confirms the user's email using a code.
     *
     * @param dto the data transfer object containing the token and email for confirmation
     */
    void confirmCode(CodeConfirmationDTO dto);

}