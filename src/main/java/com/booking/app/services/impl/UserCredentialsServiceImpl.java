package com.booking.app.services.impl;

import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.ConfirmationCode;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.enums.EnumProvider;
import com.booking.app.enums.EnumRole;
import com.booking.app.exception.exception.UserIsDisabledException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.UserCredentialsService;
import com.booking.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link UserCredentialsService} for managing user credentials.
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Log4j2
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ConfirmationCodeService confirmationCodeService;

    @Override
    public Optional<UserCredentials> findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email);
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

    @Override
    public void enableUserCredentials(UserCredentials userCredentials) {
        userCredentialsRepository.enableUser(userCredentials.getId());
        log.info("User with ID: {} has been enabled", userCredentials.toString());
    }

    @Override
    public UserCredentials createUserCredentials(RegistrationDTO dto) {
        UserCredentials userCredentials = mapper.toUserSecurity(dto);
        userCredentials.setProvider(EnumProvider.LOCAL);
        userCredentials.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findRoleByEnumRole(EnumRole.USER);

        ConfirmationCode confirmationCode = ConfirmationCode.createCode();

        User newUser = User.createUser(role, confirmationCode, dto.getNotification());
        userCredentials.setUser(newUser);
        save(userCredentials);
        return userCredentials;
    }

    @Override
    public UserCredentials updateUserCredentials(UserCredentials userCredentials, RegistrationDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        updatePassword(userCredentials.getId(), encodedPassword);

        ConfirmationCode newCode = ConfirmationCode.createCode();
        ConfirmationCode existingCode = userCredentials.getUser().getConfirmationCode();
        confirmationCodeService.updateConfirmationCode(newCode, existingCode);

        userService.updateNotification(userCredentials.getUser().getId(), dto.getNotification());
        return userCredentials;
    }

    @Override
    public void delete(UserCredentials userCredentials){
        userCredentialsRepository.delete(userCredentials);
    }

}
