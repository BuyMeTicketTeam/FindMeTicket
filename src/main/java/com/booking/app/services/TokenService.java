package com.booking.app.services;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;

public interface TokenService {

    public boolean verifyToken(String email, String givenToken);

    public ConfirmToken createConfirmToken(User user);

    public String generateRandomToken();
}
