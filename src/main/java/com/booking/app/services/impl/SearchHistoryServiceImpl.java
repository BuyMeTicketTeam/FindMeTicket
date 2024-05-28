package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.SearchHistory;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.entity.User;
import com.booking.app.enums.TypeTransportEnum;
import com.booking.app.mapper.HistoryMapper;
import com.booking.app.mapper.model.ArrivalCity;
import com.booking.app.mapper.model.DepartureCity;
import com.booking.app.repositories.SearchHistoryRepository;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.SearchHistoryService;
import com.booking.app.services.TypeAheadService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository historyRepository;

    private final UserRepository userRepository;

    private final TypeAheadService typeAheadService;

    private final HistoryMapper historyMapper;

    private final UkrPlacesRepository ukrPlacesRepository;

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
        return Stream.of(user)
                .flatMap(u -> u.getHistory().stream())
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
