package com.booking.app.services.impl;

import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.ConfirmationCode;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.enums.EnumProvider;
import com.booking.app.enums.EnumRole;
import com.booking.app.exception.exception.UserIsDisabledException;
import com.booking.app.mapper.UserMapper;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationCodeService confirmationCodeService;

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
    public boolean isEnabled(User user) throws UserIsDisabledException {
        if (user.isEnabled()) {
            return true;
        } else {
            throw new UserIsDisabledException();
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
        User user = userMapper.toUser(dto);
        user.setProvider(EnumProvider.LOCAL);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findRoleByEnumRole(EnumRole.USER);

        ConfirmationCode confirmationCode = ConfirmationCode.createCode();

        User newUser = User.createBasicUser(role, confirmationCode, dto.getNotification());

        save(newUser);
        return newUser;
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
