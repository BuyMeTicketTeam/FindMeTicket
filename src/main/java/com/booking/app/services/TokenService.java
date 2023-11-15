package com.booking.app.services;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;

import java.util.function.Predicate;

public interface TokenService {
    public boolean verifyToken(String email, String givenToken);
    public ConfirmToken createConfirmToken(User user);
    public String generateRandomToken();
}
