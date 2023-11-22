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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

/**
 * Service class for sending email.
 */
@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserSecurityRepository userSecurityRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final TokenService tokenService;

    /**
     * This method sending email with specified token.
     *
     * @param htmlPage String html representation of the letter
     * @param subject String subject of the letter
     * @param token String generated token that is needed to confirm email
     * @param user UserSecurity recipient
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
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

    /**
     * This method generates new token end sends it
     * on specified email
     *
     * @param email String recipient
     * @return Boolean returns true if the message was sent successfully either returns false
     * @throws UserPrincipalNotFoundException If there is no such user
     * @throws MessagingException             If there is an issue with sending the confirmation email.
     */
    @Transactional
    @Override
    public boolean resendEmail(String email) throws MessagingException, UserPrincipalNotFoundException {
        Optional<UserSecurity> userByEmailFromDb = userSecurityRepository.findByEmail(email);

        if(userByEmailFromDb.isEmpty()) return false;

        User user = userByEmailFromDb.get().getUser();
        verifyEmailRepository.delete(user.getConfirmToken());

        ConfirmToken confirmToken = tokenService.createConfirmToken(user);
        user.setConfirmToken(confirmToken);

        verifyEmailRepository.save(confirmToken);
        sendEmail("confirmMail", "Email confirmation", confirmToken.getToken(), userByEmailFromDb.get());
        return true;
    }
}
