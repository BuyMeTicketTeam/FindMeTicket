package com.booking.app.services;

import com.booking.app.dto.SocialLoginDto;
import com.booking.app.entities.user.User;

import java.util.Optional;

public interface GoogleAccountService {
    Optional<User> login(SocialLoginDto requestBody);
}
