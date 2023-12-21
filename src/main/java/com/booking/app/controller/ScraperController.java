package com.booking.app.controller;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.Ticket;
import com.booking.app.services.impl.ScrapingServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
public class ScraperController {

    private ScrapingServiceImpl scrapingService;

    @PostMapping("/searchtickets")
    SseEmitter findTickets(@RequestBody RequestTicketsDTO ticketsDTO) throws IOException {
        SseEmitter emitter = new SseEmitter();

        scrapingService.scrapFromBusfor(ticketsDTO, emitter);

        return emitter;
    }

    @GetMapping("/get/ticket/{id}")
    SseEmitter getTicket(@PathVariable UUID id) throws IOException {
        SseEmitter emitter = new SseEmitter();

        scrapingService.getTicket(id, emitter);

        return emitter;
    }

}
