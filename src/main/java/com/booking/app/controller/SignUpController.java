package com.booking.app.controller;


import com.booking.app.controller.api.UserAuthAPI;
import com.booking.app.controller.dto.*;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.services.UserSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@AllArgsConstructor
public class SignUpController implements UserAuthAPI {

    private final UserSecurityService service;

    @PreAuthorize("hasRole(USER)")
    @PostMapping
    @Override
    public ResponseDTO<LoginDTO> signUp(@RequestBody RegistrationDTO dto) throws UserAlreadyExistAuthenticationException {
        return new ResponseDTO<>(service.register(dto));
    }


    @GetMapping
    @Override
    public ResponseDTO<LoginDTO> signIn(LoginDTO dto) {
        return null;
    }

}
