package com.booking.app.services;

import com.booking.app.dto.OAuth2IdTokenDTO;
import com.booking.app.entity.UserCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public interface GoogleAccountService {
    Optional<UserCredentials> loginOAuthGoogle(OAuth2IdTokenDTO requestBody) throws GeneralSecurityException, IOException;
}
