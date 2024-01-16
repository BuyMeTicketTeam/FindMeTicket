package com.booking.app.services.impl;

import com.booking.app.entity.UserCredentials;
import com.booking.app.repositories.UserRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.security.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    CustomUserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;


    @Test
    void testSuccessfullyLoadUserByUsername() {

        String email = "javier_milei@gmail.com";

        UserCredentials userCredentials = new UserCredentials();

        when(userCredentialsRepository.findByEmail(email)).thenReturn(Optional.of(userCredentials));

        Assertions.assertNotNull(userDetailsService.loadUserByUsername(email));
    }

    @Test
    void testNotFoundLoadUserByUsername() {

        String email = "javier_milei@gmail.com";

        when(userCredentialsRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, ()->userDetailsService.loadUserByUsername(email));
    }

}