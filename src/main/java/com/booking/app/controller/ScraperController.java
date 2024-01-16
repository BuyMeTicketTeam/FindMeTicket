package com.booking.app.controller;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.Ticket;
import com.booking.app.services.impl.ScrapingServiceImpl;
import com.booking.app.services.impl.SortedTicketsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping
@AllArgsConstructor
// TODO: Describe API's for controller in interfaces
public class ScraperController {

    private ScrapingServiceImpl scrapingService;
    private SortedTicketsServiceImpl sortedTicketsService;

    @PostMapping("/searchTickets")
    public SseEmitter findTickets(@RequestBody RequestTicketsDTO ticketsDTO) throws IOException, ParseException, InterruptedException {

        SseEmitter emitter = new SseEmitter();

        scrapingService.scrapeTickets(ticketsDTO, emitter);

        return emitter;
    }

    @GetMapping("/get/ticket/{id}")
    public SseEmitter getTicket(@PathVariable UUID id) throws IOException {
        SseEmitter emitter = new SseEmitter();

        //scrapingService.getTicket(id, emitter);

        return emitter;
    }

    @PostMapping("/sortedBy")
    public ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO){
        return ResponseEntity.ok().body(sortedTicketsService.getSortedTickets(requestSortedTicketsDTO));
    }

}
