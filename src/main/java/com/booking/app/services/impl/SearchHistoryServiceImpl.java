package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.dto.StartLettersDTO;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.entity.UserSearchHistory;
import com.booking.app.mapper.HistoryMapper;
import com.booking.app.repositories.SearchHistoryRepository;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.SearchHistoryService;
import com.booking.app.services.TypeAheadService;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.booking.app.constant.CustomHttpHeaders.USER_ID;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository historyRepository;

    private final UserCredentialsRepository userRepository;

    private final TypeAheadService typeAheadService;

    private final HistoryMapper historyMapper;

    private final UkrPlacesRepository ukrPlacesRepository;

    public void addToHistory(RequestTicketsDTO requestTicketsDTO, String language, HttpServletRequest request) {
        Optional<UUID> userId = getIdFromRequest(request);

        Optional<UserCredentials> user = userId.flatMap(userRepository::findById);
        user.ifPresent(t -> historyRepository.save(UserSearchHistory.builder()
                .user(t.getUser())
                .departureCityId(typeAheadService.getCityId(requestTicketsDTO.getDepartureCity(), language))
                .arrivalCityId(typeAheadService.getCityId(requestTicketsDTO.getArrivalCity(), language))
                .departureDate(requestTicketsDTO.getDepartureDate())
                .build()));
    }


    public List<SearchHistoryDto> getUserHistory(HttpServletRequest request) {
        return getIdFromRequest(request)
                .flatMap(userRepository::findById)
                .map(user -> user.getUser().getHistory().stream().map(t -> historyToDto(t, request.getHeader(HttpHeaders.CONTENT_LANGUAGE))).collect(Collectors.toList()))
                .orElseThrow(() -> new UsernameNotFoundException("No such user"));
    }

    private SearchHistoryDto historyToDto(UserSearchHistory userSearchHistory, String language) {
        String cityTo = getCity(userSearchHistory.getArrivalCityId(), language);
        String cityFrom = getCity(userSearchHistory.getDepartureCityId(), language);

        return SearchHistoryDto.builder()
                .addingTime(userSearchHistory.getAddingTime().format(DateTimeFormatter.ofPattern("hh:mm, dd/MM/yyyy")))
                .departureDate(userSearchHistory.getDepartureDate())
                .arrivalCity(cityTo)
                .departureCity(cityFrom).build();
    }

    private String getCity(Long id, String language) {
        return language.equals("eng") ? ukrPlacesRepository.findById(id).get().getNameEng()
                : ukrPlacesRepository.findById(id).get().getNameEng();
    }

    private Optional<UUID> getIdFromRequest(HttpServletRequest request) {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, USER_ID);
        return cookie.map(c -> {
            try {
                return Optional.of(UUID.fromString(c.getValue()));
            } catch (IllegalArgumentException e) {
                return Optional.<UUID>empty();
            }
        }).orElse(Optional.empty());
    }


}