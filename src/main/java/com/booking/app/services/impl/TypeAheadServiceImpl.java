package com.booking.app.services.impl;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.services.LanguageDetectorService;
import com.booking.app.services.TypeAheadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for providing type-ahead suggestions for cities based on start letters.
 */
@Service
@RequiredArgsConstructor
public class TypeAheadServiceImpl implements TypeAheadService {

    private final UkrPlacesRepository ukrPlacesRepository;

    private final LanguageDetectorService languageDetectorService;


    /**
     * Find matching cities in Ukraine based on start letters and site language.
     *
     * @param startLetters DTO containing the start letters for matching cities.
     * @param siteLanguage Language of the site.
     * @return List of CitiesDTO representing matching cities.
     * @throws IOException If an I/O error occurs during the process.
     */
    @Override
    public List<CitiesDTO> findMatchesUA(StartLettersDTO startLetters, String siteLanguage) throws IOException {
        String inputLanguage = languageDetectorService.detectLanguage(startLetters.getStartLetters()).orElse(null);

        if (inputLanguage == null) return Collections.emptyList();

        Optional<List<UkrainianPlaces>> listOfPlaces = getListOfPlaces(startLetters, inputLanguage);

        return listOfPlaces.map(places -> mapToCitiesDTO(places, siteLanguage, inputLanguage))
                .orElse(Collections.emptyList());
    }

    /**
     * Get the list of places based on start letters and input language.
     *
     * @param startLetters  DTO containing the start letters for matching places.
     * @param inputLanguage Detected input language.
     * @return Optional list of UkrainianPlaces matching the criteria.
     */
    private Optional<List<UkrainianPlaces>> getListOfPlaces(StartLettersDTO startLetters, String inputLanguage) {
        return (inputLanguage.equals("eng"))
                ? ukrPlacesRepository.findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContaining(startLetters.getStartLetters(), "Oblast’")
                : ukrPlacesRepository.findUkrainianPlacesByNameUaStartsWithIgnoreCaseAndNameUaNotContaining(startLetters.getStartLetters(), "Область");
    }

    /**
     * Map UkrainianPlaces entities to CitiesDTO based on site language and input language.
     *
     * @param ukrainianPlaces List of UkrainianPlaces entities.
     * @param siteLanguage    Language of the site.
     * @param inputLanguage   Detected input language.
     * @return List of CitiesDTO representing mapped cities.
     */
    private List<CitiesDTO> mapToCitiesDTO(List<UkrainianPlaces> ukrainianPlaces, String siteLanguage, String inputLanguage) {
        ukrainianPlaces.sort(Comparator.comparing(UkrainianPlaces::getNameUa));

        String country = ukrainianPlaces.stream().map(UkrainianPlaces::getCountry).findFirst().get();

        return ukrainianPlaces.stream()
                .map(place -> createCitiesDTO(place, country, siteLanguage, inputLanguage))
                .collect(Collectors.toList());
    }

    /**
     * Create a CitiesDTO based on UkrainianPlaces entity and language preferences.
     *
     * @param place         UkrainianPlaces entity.
     * @param country       Country associated with the places.
     * @param siteLanguage  Language of the site.
     * @param inputLanguage Detected input language.
     * @return CitiesDTO representing the mapped city.
     */
    private CitiesDTO createCitiesDTO(UkrainianPlaces place, String country, String siteLanguage, String inputLanguage) {

        if (siteLanguage.contains("ua")) {
            return (inputLanguage.equals("ua"))
                    ? CitiesDTO.builder().country(country).cityUa(place.getNameUa()).siteLanguage(siteLanguage).build()
                    : CitiesDTO.builder().country(country).cityEng(place.getNameEng()).cityUa(place.getNameUa()).siteLanguage(siteLanguage).build();
        } else {
            return (inputLanguage.equals("eng"))
                    ? CitiesDTO.builder().country(country).cityEng(place.getNameEng()).siteLanguage(siteLanguage).build()
                    : CitiesDTO.builder().country(country).cityEng(place.getNameEng()).cityUa(place.getNameUa()).siteLanguage(siteLanguage).build();
        }
    }

}





