package com.booking.app.services;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.User;

import java.util.List;

public interface SearchHistoryService {

    /**
     * Adds a search history entry for the user.
     *
     * @param dto      The DTO containing search details.
     * @param language The language of the search.
     * @param user     User to add history
     */
    void addHistory(RequestTicketsDTO dto, String language, User user);

    /**
     * Retrieves the search history of a user.
     *
     * @param user     The user credentials.
     * @param language The language for city names.
     * @return List of search history DTOs.
     */
    List<SearchHistoryDto> getHistory(User user, String language);

}
