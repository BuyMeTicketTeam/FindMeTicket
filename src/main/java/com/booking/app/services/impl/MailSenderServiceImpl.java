package com.booking.app.services.impl;

import com.booking.app.entity.UserSecurity;
import com.booking.app.services.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;

@Service
@AllArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendMessage(UserSecurity user) throws IOException, MessagingException {

    }

    @Override
    public void sendEmailRecoverPassword(String contextPath, String token, UserSecurity user) {
        //     mailSender.send(constructResetTokenEmail(contextPath, token, user));
    }

    @Override
    public void sendEmailWithActivationToken(String token, UserSecurity user) throws IOException, MessagingException {
        Context context = new Context();
        context.setVariable("nickname", user.getUsername());
        context.setVariable("token", token);

        String process = templateEngine.process("main", context);


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Email confirmation");
        helper.setText(process, true);
        helper.setTo(user.getEmail());

        mailSender.send(mimeMessage);

        System.out.println("sent");
    }


//    private SimpleMailMessage constructResetTokenEmail(
//            String contextPath, String token, UserSecurity user) {
//        String url = contextPath + USER + USER_UPDATE_PASSWORD + "?token=" + token;
//        String message = "Change your password after clicking reference below."
//                + "If you didn't request to change password, please won't do anything";
//        return constructEmail("Reset password", message + "\r\n" + url, user);
//    }



}
