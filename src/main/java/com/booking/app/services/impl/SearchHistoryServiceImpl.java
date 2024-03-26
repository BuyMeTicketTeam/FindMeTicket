package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSearchHistory;
import com.booking.app.mapper.HistoryMapper;
import com.booking.app.repositories.SearchHistoryRepository;
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

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository historyRepository;

    private final UserRepository userRepository;

    private final TypeAheadService typeAheadService;

    private final HistoryMapper historyMapper;

    public void addToHistory(RequestTicketsDTO requestTicketsDTO, String language, HttpServletRequest request){

        UUID userId = getIdFromRequest(request);

        Optional<User> User = userRepository.findById(userId);

        User.ifPresent(t-> historyRepository.save(UserSearchHistory.builder()
                .user(t)
                .departureCityId(typeAheadService.getCityId(requestTicketsDTO.getDepartureCity(), language))
                .arrivalCityId(typeAheadService.getCityId(requestTicketsDTO.getArrivalCity(), language))
                .departureDate(requestTicketsDTO.getDepartureDate())
                .build()));
    }

    public List<SearchHistoryDto> getUserHistory(HttpServletRequest request){
        return userRepository.findById(getIdFromRequest(request)).orElseThrow(()->new UsernameNotFoundException("No such user")).getHistory().stream().map(historyMapper::historyToDto).collect(Collectors.toList());
    }

    private UUID getIdFromRequest(HttpServletRequest request){
        return UUID.fromString(Arrays.stream(request.getCookies()).filter(t->t.getName().equals("user_id")).findFirst().get().getValue());
    }
}
