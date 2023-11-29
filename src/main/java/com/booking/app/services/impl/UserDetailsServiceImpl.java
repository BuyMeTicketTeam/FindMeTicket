package com.booking.app.services.impl;

import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserRepository;
import com.booking.app.repositories.UserSecurityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserSecurityRepository userSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Entering in loadUserByUsername Method...");

        Optional<UserSecurity> userSecurity = userSecurityRepository.findByEmail(email);
        if(userSecurity.isEmpty()){
            log.error("Email not found: " + email);
            throw new UsernameNotFoundException(String.format("User %s not found", email));
        }
        log.info("User Authenticated Successfully..!!!");
        return userSecurity.get();
    }
}
