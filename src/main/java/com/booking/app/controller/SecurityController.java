package com.booking.app.controller;


import com.booking.app.controller.api.UserAuthAPI;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.ResponseDTO;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping
@AllArgsConstructor
public class SecurityController implements UserAuthAPI {

    private final UserSecurityService service;


    //  @PreAuthorize("hasRole(ROLE_USER)")
    @PostMapping("/register")
    @Override
    public ResponseDTO<LoginDTO> signUp(@RequestBody RegistrationDTO dto) throws UserAlreadyExistAuthenticationException, MessagingException, IOException {
        return new ResponseDTO<>(service.register(dto));
    }

    @PostMapping("/login")
    @Override
    public ResponseDTO<LoginDTO> signIn(LoginDTO dto) {
        return null;
    }





    @PostMapping("/reset")




}
