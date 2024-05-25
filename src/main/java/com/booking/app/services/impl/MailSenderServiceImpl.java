package com.booking.app.services.impl;

import com.booking.app.entity.ConfirmationCode;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.UserCredentialsNotFoundException;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.UserCredentialsService;
import com.booking.app.util.HtmlTemplateUtils;
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
 * Service implementation for sending emails related to user registration and confirmation.
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Log4j2
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserCredentialsService userCredentialsService;
    private final ConfirmationCodeService confirmationCodeService;

    @Override
    public void sendEmail(String language, String subject, String token, UserCredentials userCredentials) {
        String htmlPageName = HtmlTemplateUtils.getConfirmationHtmlTemplate(language);
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("nickname", userCredentials.getUsername());

        String process = templateEngine.process(htmlPageName, context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject(subject);
            helper.setText(process, true);
            helper.setTo(userCredentials.getEmail());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email to {}, reason: {}", userCredentials.getEmail(), e.getCause());
        }
    }

    @Override
    public void resendEmail(String language, String email) {
        userCredentialsService.findByEmail(email)
                .ifPresentOrElse(userCredentials -> {
                    User user = userCredentials.getUser();

                    ConfirmationCode newCode = ConfirmationCode.createCode();
                    ConfirmationCode existingCode = user.getConfirmationCode();
                    user.setConfirmationCode(newCode);

                    confirmationCodeService.updateConfirmationCode(newCode, existingCode);

                    sendEmail(language, EMAIL_CONFIRMATION_SUBJECT, newCode.getCode(), userCredentials);
                }, () -> {
                    throw new UserCredentialsNotFoundException();
                });
    }

}
