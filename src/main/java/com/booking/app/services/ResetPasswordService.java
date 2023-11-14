package com.booking.app.services;

import com.booking.app.dto.ResetPasswordDTO;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface ResetPasswordService {
     boolean sendEmailResetPassword(String email) throws MessagingException, IOException;
     boolean resetPassword(ResetPasswordDTO dto);

}
