package com.booking.app.services;

import com.booking.app.dto.users.AuthenticatedUserDto;
import com.booking.app.dto.users.BasicLoginDto;
import com.booking.app.dto.users.SocialLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

    AuthenticatedUserDto loginWithEmailAndPassword(BasicLoginDto basicLoginDTO, HttpServletResponse response);

    AuthenticatedUserDto loginWithGoogle(SocialLoginDto tokenDTO, HttpServletRequest request, HttpServletResponse response);
}
