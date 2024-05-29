package com.booking.app.services.impl;

import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.*;
import com.booking.app.enums.EnumRole;
import com.booking.app.enums.SocialProvider;
import com.booking.app.enums.TypeTransportEnum;
import com.booking.app.exception.exception.UserIsDisabledException;
import com.booking.app.mapper.HistoryMapper;
import com.booking.app.mapper.UserMapper;
import com.booking.app.mapper.model.ArrivalCity;
import com.booking.app.mapper.model.DepartureCity;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.SearchHistoryRepository;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.ConfirmationCodeService;
import com.booking.app.services.TypeAheadService;
import com.booking.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final SearchHistoryRepository historyRepository;
    private final TypeAheadService typeAheadService;
    private final HistoryMapper historyMapper;
    private final UkrPlacesRepository ukrPlacesRepository;

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
        user.setProvider(SocialProvider.LOCAL);
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

    @Override
    public void addHistory(RequestTicketsDTO dto, String language, @Nullable User user) {
        Optional.ofNullable(user).ifPresent(u -> {
            Set<TypeTransportEnum> types = TypeTransportEnum.getTypes(dto.getBus(), dto.getTrain(), dto.getAirplane(), dto.getFerry());
            historyRepository.save(SearchHistory.builder()
                    .user(u)
                    .departureCityId(typeAheadService.getCityId(dto.getDepartureCity(), language))
                    .arrivalCityId(typeAheadService.getCityId(dto.getArrivalCity(), language))
                    .departureDate(dto.getDepartureDate())
                    .typeTransport(types)
                    .build());
        });
    }

    @Override
    public List<SearchHistoryDto> getHistory(@Nullable User user, String language) {
        return Optional.ofNullable(user)
                .map(User::getHistory)
                .orElse(Collections.emptyList())
                .stream()
                .map(history -> {
                    String departureCity = getCity(history.getArrivalCityId(), language);
                    String arrivalCity = getCity(history.getDepartureCityId(), language);
                    return historyMapper.historyToDto(history, new DepartureCity(arrivalCity), new ArrivalCity(departureCity));
                })
                .toList().reversed();
    }

    /**
     * Retrieves the name of a city based on its ID and language.
     *
     * @param id       The ID of the city.
     * @param language The language for the city name.
     * @return The name of the city.
     */
    private String getCity(Long id, String language) {
        Optional<String> city = language.equals("eng")
                ? ukrPlacesRepository.findById(id).map(UkrainianPlaces::getNameEng)
                : ukrPlacesRepository.findById(id).map(UkrainianPlaces::getNameUa);
        return city.orElse(null);
    }

}
