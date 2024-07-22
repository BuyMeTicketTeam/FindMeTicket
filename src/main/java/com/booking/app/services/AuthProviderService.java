package com.booking.app.services;

import com.booking.app.entities.user.AuthProvider;

public interface AuthProviderService {

    AuthProvider findByType(AuthProvider.AuthProviderType type);

}
