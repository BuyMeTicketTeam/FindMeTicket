package com.booking.app.controller;

import com.booking.app.controller.api.ScraperAPI;
import com.booking.app.dto.BusTicketDTO;
import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.services.SortTicketsService;
import com.booking.app.services.TicketService;
import com.booking.app.services.impl.ScraperServiceImpl;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ScraperController implements ScraperAPI {

    private final ScraperServiceImpl scrapingService;

    private final SortTicketsService sortTicketsService;

    private final TicketService ticketService;

    @PostMapping(value = "/searchTickets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter findTickets(@RequestBody RequestTicketsDTO ticketsDTO, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        SseEmitter emitter = new SseEmitter();
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        CompletableFuture<Boolean> isTicketsFound = scrapingService.scrapeTickets(ticketsDTO, emitter, siteLanguage);

        isTicketsFound.thenAccept(isFound -> {
            if (isFound) response.setStatus(HttpStatus.OK.value());
            else response.setStatus(HttpStatus.NOT_FOUND.value());
        });
        return emitter;
    }

    @GetMapping(value = "/get/ticket/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<ResponseBodyEmitter> getTicket(@PathVariable UUID id, HttpServletRequest request) throws IOException, ParseException {
        SseEmitter emitter = new SseEmitter();
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        scrapingService.getTicket(id, emitter, siteLanguage);

        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @PostMapping(value = "/sortedBy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO, HttpServletRequest request) {
        String siteLanguage = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
        return ResponseEntity.ok().body(sortTicketsService.getSortedTickets(requestSortedTicketsDTO, siteLanguage));
    }

    @PostMapping(value = "/selectedTransport", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusTicketDTO>> getSelectedTransportTicket(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO, HttpServletRequest request) {
        return ResponseEntity.ok(ticketService.getBusTickets(requestSortedTicketsDTO));
    }

}
