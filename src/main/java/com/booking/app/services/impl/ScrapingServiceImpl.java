package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Log4j2
public class ScrapingServiceImpl {

    private final BusforScrapeServiceImpl busforScrapeService;
    private final InfobusScrapeServiceImpl infobusScrapeService;
    private final ProizdScrapeServiceImpl proizdScrapeService;
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Async
    public void scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter) throws IOException, ParseException, InterruptedException {

        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

        if (route == null) {

            Route newRoute = Route.builder()
                    .addingTime(LocalDateTime.now())
                    .departureCity(requestTicketDTO.getDepartureCity())
                    .arrivalCity(requestTicketDTO.getArrivalCity())
                    .departureDate(requestTicketDTO.getDepartureDate())
                    .ticketList(new LinkedList<>()).build();

            ExecutorService executorService = Executors.newFixedThreadPool(3);

            executorService.execute(() -> {
                try {
                    busforScrapeService.scrapeTickets(requestTicketDTO, emitter, newRoute);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            executorService.execute(() -> {
                try {
                    infobusScrapeService.scrapeTickets(requestTicketDTO, emitter, newRoute);
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            });

            executorService.execute(() -> {
                try {
                    proizdScrapeService.scrapeTickets(requestTicketDTO, emitter, newRoute);
                } catch (IOException | ParseException e) {
                }
            });

            executorService.shutdown();

            try {

                if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                }
            } catch (InterruptedException e) {
            }
            routeRepository.save(newRoute);

        } else {
            for (Ticket ticket : route.getTicketList()) {
                emitter.send(SseEmitter.event().name("ticket data: ").data(toDTO(ticket, route)));
            }
        }

        emitter.complete();

    }


    private TicketDTO toDTO(Ticket ticket, Route route) {
        TicketDTO result = ticketMapper.toTicketDTO(ticket);
        result.setDepartureCity(route.getDepartureCity());
        result.setArrivalCity(route.getArrivalCity());
        result.setDepartureDate(route.getDepartureDate());
        return result;
    }

    @Async
    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void deleteOldTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now();

        routeRepository.deleteRouteByAddingTimeBefore(fifteenMinutesAgo);
    }

}
