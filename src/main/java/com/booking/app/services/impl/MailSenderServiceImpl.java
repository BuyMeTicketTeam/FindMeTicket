package com.booking.app.services.impl;

import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.services.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {
    @Value("${TOKEN_SYMBOLS}")
    private String TOKEN_SYMBOLS;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserSecurityRepository userSecurityRepository;




    @Override
    public void sendEmailRecoverPassword(String token, UserSecurity user) {

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
    }
    public void sendEmail(String email) throws IOException, MessagingException {
        Optional<UserSecurity> byEmail = userSecurityRepository.findByEmail(email);
        if(byEmail.isPresent()) {
            sendEmailWithActivationToken(generateRandomToken(), byEmail.get());
        }
    }

    public String generateRandomToken() {
        final SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(TOKEN_SYMBOLS.length());
            char randomChar = TOKEN_SYMBOLS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }



//    private SimpleMailMessage constructResetTokenEmail(
//            String contextPath, String token, UserSecurity user) {
//        String url = contextPath + USER + USER_UPDATE_PASSWORD + "?token=" + token;
//        String message = "Change your password after clicking reference below."
//                + "If you didn't request to change password, please won't do anything";
//        return constructEmail("Reset password", message + "\r\n" + url, user);
//    }



}
