package com.booking.app.services.impl;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Optional;

/**
 * Service class for sending email.
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserCredentialsRepository userCredentialsRepository;
    private final VerifyEmailRepository verifyEmailRepository;

    /**
     * This method sending email with specified token.
     *
     * @param htmlPage String html representation of the letter
     * @param subject  String subject of the letter
     * @param token    String generated token that is needed to confirm email
     * @param user     UserSecurity recipient
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    @Override
    public void sendEmail(String htmlPage, String subject, String token, UserCredentials user) throws MessagingException {

        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("nickname", user.getUsername());

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
     * @throws MessagingException If there is an issue with sending the confirmation email.
     */
    @Override
    public boolean resendEmail(String email, String htmlPage) throws MessagingException {
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByEmail(email);

        if (userCredentials.isEmpty()) return false;

        User user = userCredentials.get().getUser();
        verifyEmailRepository.delete(user.getConfirmToken());

        ConfirmToken confirmToken = ConfirmToken.createConfirmToken();
        confirmToken.setUser(user);
        user.setConfirmToken(confirmToken);

        verifyEmailRepository.save(confirmToken);

        sendEmail(htmlPage, "Email confirmation", confirmToken.getToken(), userCredentials.get());
        return true;
    }

}
