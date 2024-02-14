package com.booking.app.security;

import com.booking.app.entity.UserCredentials;
import com.booking.app.repositories.UserCredentialsRepository;
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
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByEmail(email);
        if(userCredentials.isEmpty()){
            throw new UsernameNotFoundException(String.format("User %s not found", email));
        }
        return userCredentials.get();
    }

}
