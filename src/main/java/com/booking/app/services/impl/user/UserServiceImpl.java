package com.booking.app.services.impl.user;

import com.booking.app.dto.CodeConfirmationDto;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entities.user.AuthProvider;
import com.booking.app.entities.user.ConfirmationCode;
import com.booking.app.entities.user.Role;
import com.booking.app.entities.user.User;
import com.booking.app.exceptions.user.UserNotConfirmedException;
import com.booking.app.exceptions.user.UserNotFoundException;
import com.booking.app.repositories.ProviderRepository;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for managing user operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final ConfirmationCodeService confirmationCodeService;

    private final PasswordEncoder passwordEncoder;

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void confirmCode(CodeConfirmationDto dto) {
        findByEmail(dto.getEmail())
                .ifPresentOrElse(user -> {
                    if (!user.isEnabled()) {
                        confirmationCodeService.verifyCode(user.getConfirmationCode(), dto.getCode());
                        enableUser(user);
                        log.info("User with ID: {} has successfully confirmed email.", user.getId());
                    }
                }, () -> {
                    throw new UserNotFoundException();
                });
    }

    @Override
    public void updateNotification(UUID uuid, boolean notification) {
        userRepository.updateByNotification(uuid, notification);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isEnabled(User user) throws UserNotConfirmedException {
        if (user.isEnabled()) {
            return true;
        } else {
            throw new UserNotConfirmedException();
        }
    }

    @Override
    public void updatePassword(UUID uuid, String encodedPassword) {
        userRepository.updatePassword(uuid, encodedPassword);
        log.info("User with ID: {} has successfully updated password", uuid.toString());
    }

    @Override
    public void enableUser(User user) {
        userRepository.enableUser(user.getId());
        log.info("User with ID: {} has been enabled", user);
    }

    @Override
    public User saveUser(RegistrationDTO dto) {
        Role role = roleRepository.findByType(Role.RoleType.USER);
        AuthProvider provider = providerRepository.findByType(AuthProvider.AuthProviderType.BASIC);
        ConfirmationCode confirmationCode = ConfirmationCode.createCode();
        User user = User.createBasicUser(
                provider,
                role,
                confirmationCode, dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getUsername(), dto.getNotification()
        );
        return save(user);
    }

    @Override
    public User updateUser(User user, RegistrationDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        updatePassword(user.getId(), encodedPassword);

        ConfirmationCode newCode = ConfirmationCode.createCode();
        ConfirmationCode existingCode = user.getConfirmationCode();
        confirmationCodeService.updateConfirmationCode(newCode, existingCode);

        updateNotification(user.getId(), dto.getNotification());
        return user;
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

}
