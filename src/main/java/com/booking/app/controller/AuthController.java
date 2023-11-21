package com.booking.app.controller;

import com.booking.app.controller.api.AuthAPI;
import com.booking.app.dto.*;
import com.booking.app.services.UserSecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import java.util.Arrays;

@RestController
@RequestMapping
@AllArgsConstructor
@Data
public class AuthController implements AuthAPI {

    private final UserSecurityService service;


    @PostMapping("/login")
    @Override
    public ResponseEntity<?> signIn(@RequestBody LoginDTO dto , HttpServletRequest request, HttpServletResponse response) {
            return ResponseEntity.ok().build();
      }
       // Arrays.stream(cookies).forEach(cookie -> cookie.getAttribute("Authorization"));
//        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge((int) jwtUtil.getRefreshTokenExpiration() / 1000);
//        cookie.setPath("/");
//        response.addCookie(cookie);

    }

//    @PostMapping("/test")
//    public ResponseEntity<?> test(HttpServletResponse response) {
//        response.addCookie();
//        response.setHeader();
//
//        Cookie cookie = new Cookie("jwt", "jwt");
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
//        return ResponseEntity.status(HttpStatus.OK).build();
//
//    }

