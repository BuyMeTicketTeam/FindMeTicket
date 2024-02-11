package com.booking.app.controller;

import com.booking.app.controller.api.ScraperAPI;
import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.services.SortTicketsService;
import com.booking.app.services.impl.ScraperServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ScraperController implements ScraperAPI {

    private final ScraperServiceImpl scrapingService;

    private final SortTicketsService sortTicketsService;

    @PostMapping("/searchTickets")
    public ResponseEntity<ResponseBodyEmitter> findTickets(@RequestBody RequestTicketsDTO ticketsDTO, HttpServletRequest request) throws IOException, ParseException, ExecutionException, InterruptedException {
        SseEmitter emitter = new SseEmitter();
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        CompletableFuture<Boolean> isTicketsFound = scrapingService.scrapeTickets(ticketsDTO, emitter, siteLanguage);

        return isTicketsFound.get() ? new ResponseEntity<>(emitter, HttpStatus.OK)
                : new ResponseEntity<>(emitter, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/ticket/{id}")
    public ResponseEntity<ResponseBodyEmitter> getTicket(@PathVariable UUID id, HttpServletRequest request) throws IOException, ParseException {
        SseEmitter emitter = new SseEmitter();
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        scrapingService.getTicket(id, emitter, siteLanguage);

        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @PostMapping("/sortedBy")
    public ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO, HttpServletRequest request) {
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        return ResponseEntity.ok().body(sortTicketsService.getSortedTickets(requestSortedTicketsDTO, siteLanguage));
    }

}
