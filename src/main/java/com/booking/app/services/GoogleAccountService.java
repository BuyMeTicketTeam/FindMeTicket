package com.booking.app.services;

import com.booking.app.dto.SocialSignInRequestDto;
import com.booking.app.entity.User;

import java.util.Optional;

public interface GoogleAccountService {
    Optional<User> loginOAuthGoogle(SocialSignInRequestDto requestBody);
}
