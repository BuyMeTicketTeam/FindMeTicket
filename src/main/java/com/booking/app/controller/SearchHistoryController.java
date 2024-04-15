package com.booking.app.controller;

import com.booking.app.controller.api.SearchHistoryApi;
import com.booking.app.entity.UserCredentials;
import com.booking.app.services.SearchHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SearchHistoryController implements SearchHistoryApi {

    private final SearchHistoryService searchHistoryService;

    @GetMapping("/getHistory")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Override
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal UserCredentials userCredentials, HttpServletRequest request) {
        return ResponseEntity.ok().body(searchHistoryService.getUserHistory(userCredentials, request.getHeader(HttpHeaders.CONTENT_LANGUAGE)));
    }
}
