package com.booking.app.services.impl;

import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.UserIsDisabledException;
import com.booking.app.exception.exception.UserIsNotFoundException;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.services.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.booking.app.constant.RegistrationConstantMessages.THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserCredentials findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UserIsNotFoundException(THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE));
    }

    @Override
    public UserCredentials save(UserCredentials userCredentials) {
        return userCredentialsRepository.save(userCredentials);
    }

    @Override
    public boolean isEnabled(UserCredentials userCredentials) throws UserIsDisabledException {
        if (userCredentials.isEnabled()) {
            return true;
        } else {
            throw new UserIsDisabledException();
        }
    }

    @Override
    public void updatePassword(UUID uuid, String encodedPassword) {
        userCredentialsRepository.updatePassword(uuid, encodedPassword);
        log.info("User with ID: {} has successfully updated password", uuid.toString());
    }

}
