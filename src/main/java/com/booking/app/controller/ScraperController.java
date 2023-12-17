package com.booking.app.controller;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.services.impl.ScrapingServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping
@AllArgsConstructor
public class ScraperController {

    private ScrapingServiceImpl scrapingService;

    @PostMapping("/frombusfor")
    ResponseEntity<?> findTickets(@RequestBody @NotNull RequestTicketsDTO requestTicketDTO) throws ParseException {

        scrapingService.scrapFromBusfor(requestTicketDTO);

        return ResponseEntity.ok().body(scrapingService.getTicketsFromDB(requestTicketDTO));
    }

    @GetMapping("/frombusfor/nextfive")
    ResponseEntity<?>getNextFive(@RequestBody @NotNull RequestTicketsDTO requestNextTicketsDTO) throws ParseException {

        return ResponseEntity.ok().body(scrapingService.getTicketsFromDB(requestNextTicketsDTO));
    }

}
