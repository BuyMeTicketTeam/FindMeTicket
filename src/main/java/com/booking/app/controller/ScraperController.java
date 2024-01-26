package com.booking.app.controller;

import com.booking.app.controller.api.ScraperAPI;
import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.services.impl.ScrapingServiceImpl;
import com.booking.app.services.impl.SortedTicketsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ScraperController implements ScraperAPI {

    private final ScrapingServiceImpl scrapingService;

    private final SortedTicketsServiceImpl sortedTicketsService;

    @PostMapping("/searchTickets")
    public SseEmitter findTickets(@RequestBody RequestTicketsDTO ticketsDTO) throws IOException, ParseException, InterruptedException {
        SseEmitter emitter = new SseEmitter();

        scrapingService.scrapeTickets(ticketsDTO, emitter);

        return emitter;
    }

    @GetMapping("/get/ticket/{id}")
    public SseEmitter getTicket(@PathVariable UUID id) throws IOException, ParseException {
        SseEmitter emitter = new SseEmitter();

        scrapingService.getTicket(id, emitter);

        return emitter;
    }

    @PostMapping("/sortedBy")
    public ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO) {
        return ResponseEntity.ok().body(sortedTicketsService.getSortedTickets(requestSortedTicketsDTO));
    }

}
