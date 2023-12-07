package com.booking.app.services.impl;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.mapper.UkrainianPlacesMapper;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.services.TypeAheadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service implementation for type-ahead functionality.
 */
@Service
@AllArgsConstructor
public class TypeAheadServiceImpl implements TypeAheadService {

    private UkrPlacesRepository ukrPlacesRepository;

    private UkrainianPlacesMapper ukrainianPlacesMapper;

    /**
     * Fetches and maps cities based on provided start letters, ignoring case.
     *
     * @param startLetters DTO containing the start letters for city search.
     * @return List of CitiesDTO matching the provided start letters.
     */
    @Override
    public List<CitiesDTO> findMatches(RequestTypeAheadDTO startLetters) {
        Optional<List<UkrainianPlaces>> placesListByStartLetters = ukrPlacesRepository
                .findUkrainianPlacesByNameUaStartingWithIgnoreCase(startLetters.getStartLetters());

        Set<String> uniqueNames = new HashSet<>();
        List<CitiesDTO> citiesDTOList = new ArrayList<>();

        if (placesListByStartLetters.isPresent()) {
            for (UkrainianPlaces place : placesListByStartLetters.get()) {
                String nameUa = place.getNameUa();
                if (!uniqueNames.contains(nameUa) && (!nameUa.contains("область"))) {
                    uniqueNames.add(nameUa);
                    citiesDTOList.add(ukrainianPlacesMapper.ukrainianPlaceToCityDTO(place));
                }
            }
        }
        citiesDTOList.sort(Comparator.comparing(CitiesDTO::getCityUkr));

        return citiesDTOList;
    }

}
