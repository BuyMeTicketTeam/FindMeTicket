package com.booking.app.controller;

import com.booking.app.dto.RequestTicketDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.services.impl.ScrapingServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class ScraperController {

    private ScrapingServiceImpl scrapingService;
    @PostMapping("/frombusfor")
    ResponseEntity<?> findTickets(@RequestBody @NotNull RequestTicketDTO requestTicketDTO){
        return ResponseEntity.ok().body(scrapingService.scrapFromBusfor(requestTicketDTO));
    }

}
