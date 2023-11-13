package com.booking.app.services.impl;

import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final MailSenderService mailSenderService;
    private final UserSecurityRepository userSecurityRepository;

    @Override
    public boolean sendEmailResetPassword(String email){
        Optional<UserSecurity> byEmail = userSecurityRepository.findByEmail(email);
        mailSenderService.sendEmailRecoverPassword(mailSenderService.generateRandomToken(),byEmail.get());
    }
}
