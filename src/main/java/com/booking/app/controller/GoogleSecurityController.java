package com.booking.app.controller;

import com.booking.app.controller.api.GoogleApi;
import com.booking.app.services.impl.GoogleSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/googleSecurity")
@AllArgsConstructor
@Validated
public class GoogleSecurityController implements GoogleApi {


//    private GoogleSecurityService service;
//
//    @GetMapping
//    public SuccessSignInDto authenticate(@RequestParam @NotBlank String idToken) {
//        return service.authenticate(idToken);
//    }
}
