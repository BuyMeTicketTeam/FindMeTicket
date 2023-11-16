package com.booking.app.services.impl;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.TokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserSecurityRepository userSecurityRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final TokenService tokenService;

    @Transactional
    @Override
    public void sendEmail(String htmlPage, String subject, String token, UserSecurity user) throws MessagingException {

        Context context = new Context();
        context.setVariable("token", token);

        String process = templateEngine.process(htmlPage, context);


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setText(process, true);
        helper.setTo(user.getEmail());

        mailSender.send(mimeMessage);
    }

    @Transactional
    @Override
    public void resendEmail(String email) throws UsernameNotFoundException, MessagingException, IOException {
        UserSecurity byEmail = userSecurityRepository.findByEmail(email).get();
        User user = byEmail.getUser();
        verifyEmailRepository.delete(user.getConfirmToken());

        ConfirmToken confirmToken = tokenService.createConfirmToken(user);
        user.setConfirmToken(confirmToken);

        verifyEmailRepository.save(confirmToken);
        sendEmail("confirmMail", "Email confirmation", confirmToken.getToken(), byEmail);
    }
}
