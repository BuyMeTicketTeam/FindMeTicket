package com.booking.app.services;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;

import java.io.IOException;
import java.util.List;

public interface TypeAheadService {
    /**
     * Fetches and maps cities based on provided start letters, ignoring case.
     *
     * @param letters DTO containing the start letters for city search.
     * @return List of CitiesDTO matching the provided start letters.
     */
    List<CitiesDTO> findMatchesUA(StartLettersDTO letters, String urlLanguage) throws IOException;
}
