package com.booking.app.services.impl;

import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.security.UserDetailsServiceImpl;
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
class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSecurityRepository userSecurityRepository;


    @Test
    void testSuccessfullyLoadUserByUsername() {

        String email = "javier_milei@gmail.com";

        UserSecurity userSecurity = new UserSecurity();

        when(userSecurityRepository.findByEmail(email)).thenReturn(Optional.of(userSecurity));

        Assertions.assertNotNull(userDetailsService.loadUserByUsername(email));
    }

    @Test
    void testNotFoundLoadUserByUsername() {

        String email = "javier_milei@gmail.com";

        when(userSecurityRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, ()->userDetailsService.loadUserByUsername(email));
    }

}