package com.booking.app.controller;

import com.booking.app.controller.api.LogoutAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;

public class LogoutController implements LogoutAPI {


//    @RequestMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//
//    }
}
