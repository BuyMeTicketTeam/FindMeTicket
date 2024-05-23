package com.booking.app.services;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;

import java.io.IOException;
import java.util.List;

public interface TypeAheadService {

    /**
     * Fetches and maps cities based on provided start letters, ignoring case.
     *
     * @param letters      DTO containing the start letters for city search.
     * @param siteLanguage Language of the site.
     * @return List of CitiesDTO matching the provided start letters.
     * @throws IOException If an I/O error occurs during the process.
     */
    List<CitiesDTO> findMatches(StartLettersDTO letters, String siteLanguage) throws IOException;

    /**
     * Gets the ID of a city by its name and language.
     *
     * @param city     The name of the city.
     * @param language The language of the city name.
     * @return The ID of the city, or null if not found.
     */
    Long getCityId(String city, String language);

}
