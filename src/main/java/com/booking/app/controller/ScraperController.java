package com.booking.app.controller;

import com.booking.app.controller.api.ScraperAPI;
import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.services.SortTicketsService;
import com.booking.app.services.impl.ScraperManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ScraperController implements ScraperAPI {

    private final ScraperManager scrapingService;

    private final SortTicketsService sortTicketsService;

    @PostMapping("/searchTickets")
    @Override
    public ResponseBodyEmitter findTickets(@RequestBody RequestTicketsDTO ticketsDTO, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        SseEmitter emitter = new SseEmitter();
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        CompletableFuture<Boolean> isTicketScraped = scrapingService.scrapeTickets(ticketsDTO, emitter, siteLanguage);

        isTicketScraped.thenAccept(isFound -> {
            if (isFound) response.setStatus(HttpStatus.OK.value());
            else response.setStatus(HttpStatus.NOT_FOUND.value());
        });
        return emitter;
    }

    @GetMapping("/get/ticket/{id}")
    @Override
    public ResponseBodyEmitter getTicket(@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        SseEmitter emitter = new SseEmitter();
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        CompletableFuture<Boolean> isTicketFound = scrapingService.getTicket(id, emitter, siteLanguage);

        isTicketFound.thenAccept(isFound -> {
            if (isFound) response.setStatus(HttpStatus.OK.value());
            else response.setStatus(HttpStatus.NOT_FOUND.value());
        });
        return emitter;
    }

    @PostMapping("/sortedBy")
    @Override
    public ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO, HttpServletRequest request) {
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        return ResponseEntity.ok().body(sortTicketsService.getSortedTickets(requestSortedTicketsDTO, siteLanguage));
    }

}
