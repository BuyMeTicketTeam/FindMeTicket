package com.booking.app.services.impl;

import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import com.booking.app.services.TokenService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final MailSenderService mailSenderService;
    private final UserSecurityRepository userSecurityRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final TokenService tokenService;

    @Override
    public boolean sendEmailResetPassword(String email) throws MessagingException, IOException {

        Optional<UserSecurity> byEmail = userSecurityRepository.findByEmail(email);

        if(byEmail.isPresent()) {

            User user = byEmail.get().getUser();

            //verifyEmailRepository.delete(user.getConfirmToken());
            //String token = tokenService.generateRandomToken();
            ConfirmToken confirmToken = tokenService.createConfirmToken(user);
            user.setConfirmToken(confirmToken);

           // userSecurityRepository.save(user.getSecurity());
            verifyEmailRepository.save(confirmToken);
            mailSenderService.sendEmail("resetPassword", "Reset password", confirmToken.getToken(), byEmail.get());
            return true;
        }
        return false;

    }

    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordDTO dto) {

        if (!tokenService.verifyToken(dto.getEmail(), dto.getToken())) return false;

        UserSecurity byEmail = userSecurityRepository.findByEmail(dto.getEmail()).get();

        byEmail.setPassword(dto.getPassword());
        userSecurityRepository.save(byEmail);
        //userSecurityRepository.changePasswordById(byEmail.getId(), dto.getPassword());
        return true;
    }
}
