package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.entity.UserSearchHistory;
import com.booking.app.mapper.HistoryMapper;
import com.booking.app.repositories.SearchHistoryRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.SearchHistoryService;
import com.booking.app.services.TypeAheadService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.booking.app.constant.CustomHttpHeaders.USER_ID;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository historyRepository;

    private final UserCredentialsRepository userRepository;

    private final TypeAheadService typeAheadService;

    private final HistoryMapper historyMapper;

    public void addToHistory(RequestTicketsDTO requestTicketsDTO, String language, HttpServletRequest request){

        UUID userId = getIdFromRequest(request);

        Optional<UserCredentials> User = userRepository.findById(userId);

        User.ifPresent(t-> historyRepository.save(UserSearchHistory.builder()
                .user(t.getUser())
                .departureCityId(typeAheadService.getCityId(requestTicketsDTO.getDepartureCity(), language))
                .arrivalCityId(typeAheadService.getCityId(requestTicketsDTO.getArrivalCity(), language))
                .departureDate(requestTicketsDTO.getDepartureDate())
                .build()));
    }

    public List<SearchHistoryDto> getUserHistory(HttpServletRequest request){
        return userRepository.findById(getIdFromRequest(request)).orElseThrow(()->new UsernameNotFoundException("No such user")).getUser().getHistory().stream().map(historyMapper::historyToDto).collect(Collectors.toList());
    }

    private UUID getIdFromRequest(HttpServletRequest request){
        return UUID.fromString(Arrays.stream(request.getCookies()).filter(t->t.getName().equals(USER_ID)).findFirst().get().getValue());
    }
}
