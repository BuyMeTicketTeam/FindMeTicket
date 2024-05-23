package com.booking.app.services.impl;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.mapper.UkrainianPlacesMapper;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.services.LanguageDetectorService;
import com.booking.app.services.TypeAheadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for providing type-ahead suggestions for cities based on start letters.
 */
@Service
@RequiredArgsConstructor
public class TypeAheadServiceImpl implements TypeAheadService {

    private final UkrPlacesRepository ukrPlacesRepository;

    private final LanguageDetectorService languageDetectorService;

    private final UkrainianPlacesMapper ukrainianPlacesMapper;

    @Override
    public List<CitiesDTO> findMatches(StartLettersDTO startLetters, String siteLanguage) throws IOException {
        String inputLanguage = languageDetectorService.detectLanguage(startLetters.getStartLetters()).orElse(null);
        if (inputLanguage == null) {
            return Collections.emptyList();
        }

        Optional<List<UkrainianPlaces>> listOfPlaces = getListOfPlaces(startLetters.getStartLetters(), inputLanguage);

        return listOfPlaces.map(places -> ukrainianPlacesMapper.toCitiesDTOList(places, inputLanguage, siteLanguage))
                .orElse(Collections.emptyList());
    }

    @Override
    public Long getCityId(String city, String language) {
        return getListOfPlaces(city, language)
                .map(list -> list.isEmpty() ? null : list.getFirst().getId())
                .orElse(null);
    }

    /**
     * Retrieves a list of UkrainianPlaces based on start letters and language.
     *
     * @param startLetters  The start letters of the city name.
     * @param inputLanguage The language of the city name.
     * @return Optional list of UkrainianPlaces.
     */
    private Optional<List<UkrainianPlaces>> getListOfPlaces(String startLetters, String inputLanguage) {
        return (inputLanguage.equals("eng"))
                ? ukrPlacesRepository.findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContainingIgnoreCase(startLetters, "oblast")
                : ukrPlacesRepository.findUkrainianPlacesByNameUaStartsWithIgnoreCaseAndNameUaNotContainingIgnoreCase(startLetters, "область");
    }

}
