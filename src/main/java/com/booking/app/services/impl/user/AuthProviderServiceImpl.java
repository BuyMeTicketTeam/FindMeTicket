package com.booking.app.services.impl.user;

import com.booking.app.entities.user.AuthProvider;
import com.booking.app.repositories.ProviderRepository;
import com.booking.app.services.AuthProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthProviderServiceImpl implements AuthProviderService {

    private final ProviderRepository providerRepository;

    @Override
    public AuthProvider findByType(AuthProvider.AuthProviderType type) {
        return providerRepository.findByType(type);
    }

}
