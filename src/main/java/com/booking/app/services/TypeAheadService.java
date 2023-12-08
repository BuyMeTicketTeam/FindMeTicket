package com.booking.app.services;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import java.util.List;

public interface TypeAheadService {
    /**
     * Fetches and maps cities based on provided start letters, ignoring case.
     *
     * @param letters DTO containing the start letters for city search.
     * @return List of CitiesDTO matching the provided start letters.
     */
    List<CitiesDTO> findMatches(RequestTypeAheadDTO letters);
}
