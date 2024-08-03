package com.booking.app.services.impl.user;

import com.booking.app.constants.TransportType;
import com.booking.app.dto.tickets.RequestTicketsDto;
import com.booking.app.dto.users.HistoryDto;
import com.booking.app.entities.user.SearchHistory;
import com.booking.app.entities.user.User;
import com.booking.app.mappers.SearchHistoryMapper;
import com.booking.app.mappers.model.ArrivalCity;
import com.booking.app.mappers.model.DepartureCity;
import com.booking.app.repositories.SearchHistoryRepository;
import com.booking.app.services.SearchHistoryService;
import com.booking.app.services.TypeAheadService;
import com.booking.app.services.UkrainianCitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final UkrainianCitiesService ukrainianCitiesService;
    private final TypeAheadService typeAheadService;

    private final SearchHistoryRepository searchHistoryRepository;
    private final SearchHistoryMapper searchHistoryMapper;

    @Override
    public void addHistory(RequestTicketsDto dto, String language, @Nullable User user) {
        Optional.ofNullable(user).ifPresent(u -> {
            Set<TransportType> types = TransportType.getTypes(dto.getBus(), dto.getTrain(), dto.getAirplane(), dto.getFerry());
            searchHistoryRepository.save(SearchHistory.builder()
                    .user(u)
                    .departureCityId(typeAheadService.getCityId(dto.getDepartureCity(), language))
                    .arrivalCityId(typeAheadService.getCityId(dto.getArrivalCity(), language))
                    .departureDate(dto.getDepartureDate())
                    .typeTransport(types)
                    .build());
        });
    }

    @Override
    public List<HistoryDto> getHistory(@Nullable User user, String language) {
        return Optional.ofNullable(user)
                .map(User::getHistory)
                .orElse(Collections.emptyList())
                .stream()
                .map(history -> {
                    String departureCity = ukrainianCitiesService.getCity(history.getArrivalCityId(), language);
                    String arrivalCity = ukrainianCitiesService.getCity(history.getDepartureCityId(), language);
                    return searchHistoryMapper.historyToDto(history, new DepartureCity(arrivalCity), new ArrivalCity(departureCity));
                })
                .toList().reversed();
    }

}
