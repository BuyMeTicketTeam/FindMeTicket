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
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.booking.app.constant.MailConstants.EMAIL_CONFIRMATION_SUBJECT;

/**
 * Service class for sending email.
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Log4j2
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserCredentialsRepository userCredentialsRepository;
    private final VerifyEmailRepository verifyEmailRepository;


    @Override
    public void sendEmail(String htmlPage, String subject, String token, UserCredentials user) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("nickname", user.getUsername());

        String process = templateEngine.process(htmlPage, context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject(subject);
            helper.setText(process, true);
            helper.setTo(user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email to {}, reason: {}", user.getEmail(), e);
        }

        mailSender.send(mimeMessage);
    }


    @Override
    public boolean resendEmail(String email, String htmlPage) {
        return userCredentialsRepository.findByEmail(email)
                .map(userCredentials -> {
                    User user = userCredentials.getUser();
                    verifyEmailRepository.delete(userCredentials.getUser().getConfirmToken());

                    ConfirmToken confirmToken = ConfirmToken.createConfirmToken();
                    confirmToken.setUser(user);
                    user.setConfirmToken(confirmToken);

                    verifyEmailRepository.save(confirmToken);

                    sendEmail(htmlPage, EMAIL_CONFIRMATION_SUBJECT, confirmToken.getToken(), userCredentials);
                    return true;
                }).orElse(false);
    }

}
