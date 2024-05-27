package com.booking.app.controller;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDto;
import com.booking.app.exception.exception.UndefinedLanguageException;
import com.booking.app.services.SearchHistoryService;
import com.booking.app.services.SortTicketsService;
import com.booking.app.services.TicketService;
import com.booking.app.services.impl.scrape.ScraperManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
@RequestMapping(produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
@RequiredArgsConstructor
@Tag(name = "Ticket web scraping", description = "Endpoints for scraping tickets' info")
public class TicketController {

    private final ScraperManager scrapingService;

    private final SortTicketsService sortTicketsService;

    private final TicketService ticketService;

    private final SearchHistoryService searchHistoryService;

    @PostMapping("/tickets/search")
    @Operation(summary = "Search tickets", description = "Find tickets based by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets has been found"),
            @ApiResponse(responseCode = "404", description = "Tickets is not found")
    })
    public ResponseBodyEmitter findTickets(@RequestBody @NotNull @Valid RequestTicketsDTO ticketsDTO,
                                           @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                           HttpServletResponse response,
                                           HttpServletRequest request) throws IOException, ParseException {
        validateLanguage(siteLanguage);
        searchHistoryService.addHistory(ticketsDTO, siteLanguage, request);

        SseEmitter emitter = new SseEmitter();
        CompletableFuture<Boolean> isTicketScraped = scrapingService.findTickets(ticketsDTO, emitter, siteLanguage);

        isTicketScraped.thenAccept(isFound -> {
            if (Boolean.TRUE.equals(isFound)) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        });
        return emitter;
    }

    @GetMapping("/tickets/{id}")
    @Operation(summary = "Single ticket", description = "Ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket has been found"),
            @ApiResponse(responseCode = "404", description = "Ticket is not found")
    })
    public ResponseBodyEmitter getTicketById(@PathVariable("id") UUID id,
                                             @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage,
                                             HttpServletResponse response) throws IOException, ParseException {
        validateLanguage(siteLanguage);

        SseEmitter emitter = new SseEmitter();
        CompletableFuture<Boolean> isTicketFound = scrapingService.getTicket(id, emitter, siteLanguage);

        isTicketFound.thenAccept(isFound -> {
            if (Boolean.TRUE.equals(isFound)) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        });
        return emitter;
    }

    @PostMapping(value = "/tickets/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sort tickets", description = "Either by price, travel time, departure, or arrival")
    @ApiResponse(responseCode = "200", description = "Get sorted tickets")
    public ResponseEntity<?> getSortedTickets(@RequestBody @NotNull @Valid RequestSortedTicketsDTO requestSortedTicketsDTO,
                                              @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) String siteLanguage) {
        validateLanguage(siteLanguage);
        return ResponseEntity.ok().body(sortTicketsService.getSortedTickets(requestSortedTicketsDTO, siteLanguage));
    }

    @PostMapping(value = "/tickets/transport", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get tickets", description = "Get tickets by type transport")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
            @ApiResponse(responseCode = "404", description = "No tickets of the requested type were found")
    })
    public ResponseEntity<List<TicketDto>> getTicketByType(@RequestBody @NotNull @Valid RequestTicketsDTO requestTicketsDTO) throws IOException {
        return ticketService.getTickets(requestTicketsDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void validateLanguage(String language) {
        if (language.equals("ua") || language.equals("eng")) return;
        throw new UndefinedLanguageException("Incomprehensible language passed into " + HttpHeaders.CONTENT_LANGUAGE + " (ua or eng required)");
    }

}
