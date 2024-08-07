package com.booking.app.services;

import com.booking.app.dto.tickets.RequestTicketsDto;
import com.booking.app.dto.users.HistoryDto;
import com.booking.app.entities.user.User;

import java.util.List;

public interface SearchHistoryService {

    /**
     * Adds a search history entry for the user.
     *
     * @param dto      The DTO containing search details.
     * @param language The language of the search.
     * @param user     User to add history
     */
    void addHistory(RequestTicketsDto dto, String language, User user);

    /**
     * Retrieves the search history of a user.
     *
     * @param user     The user credentials.
     * @param language The language for city names.
     * @return List of search history DTOs.
     */
    List<HistoryDto> getHistory(User user, String language);

}
