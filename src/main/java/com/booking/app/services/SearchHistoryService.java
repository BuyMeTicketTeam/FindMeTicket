package com.booking.app.services;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.UserCredentials;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SearchHistoryService {

    /**
     * Adds a search history entry for the user.
     *
     * @param dto      The DTO containing search details.
     * @param language The language of the search.
     * @param request  The HTTP request.
     */
    void addHistory(RequestTicketsDTO dto, String language, HttpServletRequest request);

    /**
     * Retrieves the search history of a user.
     *
     * @param userCredentials The user credentials.
     * @param language        The language for city names.
     * @return List of search history DTOs.
     */
    List<SearchHistoryDto> getUserHistory(UserCredentials userCredentials, String language);

}
