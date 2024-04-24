package com.booking.app.services;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.SearchHistoryDto;
import com.booking.app.entity.UserCredentials;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SearchHistoryService {

    void addHistory(RequestTicketsDTO requestTicketsDTO, String language, HttpServletRequest request);

    List<SearchHistoryDto> getUserHistory(UserCredentials userCredentials, String language);
}
