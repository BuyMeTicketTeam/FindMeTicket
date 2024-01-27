package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TicketUrls;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;


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
                    .tickets(new HashSet<>()).build();


            CompletableFuture<Boolean> infobus = infobusScrapeService.scrapeTickets(requestTicketDTO, emitter, newRoute);
            CompletableFuture<Boolean> proizd = proizdScrapeService.scrapeTickets(requestTicketDTO, emitter, newRoute);
            CompletableFuture<Boolean> busfor = busforScrapeService.scrapeTickets(requestTicketDTO, emitter, newRoute);

            while (!(infobus.isDone() && proizd.isDone() && busfor.isDone())) {

            }

            routeRepository.save(newRoute);

        } else {
            for (Ticket ticket : route.getTickets()) {
                emitter.send(SseEmitter.event().name("ticket data: ").data(ticketMapper.toDto(ticket)));
            }
        }

        emitter.complete();

    }

    @Async
    public void getTicket(UUID id, SseEmitter emitter) throws IOException, ParseException {

        Ticket ticket = ticketRepository.findById(id).get();

        synchronized (emitter) {
            emitter.send(SseEmitter.event().name("ticket info").data(ticketMapper.toDto(ticket)));
        }

        TicketUrls urls = ticket.getUrls();

        if (urls == null) {

            ticket.setUrls(new TicketUrls());
            ticket.getUrls().setTicket(ticket);

            CompletableFuture<Boolean> busfor = busforScrapeService.getTicket(emitter, ticket);
            CompletableFuture<Boolean> infobus = infobusScrapeService.getTicket(emitter, ticket);
            CompletableFuture<Boolean> proizd = proizdScrapeService.getTicket(emitter, ticket);

            while (!(busfor.isDone() && infobus.isDone() && proizd.isDone())) {
            }

            ticketRepository.save(ticket);
        } else {
            synchronized (emitter) {
                if (urls.getProizd() != null)
                    emitter.send(SseEmitter.event().name("Proizd url:").data(ticket.getUrls().getProizd()));
                if (urls.getBusfor() != null)
                    emitter.send(SseEmitter.event().name("Busfor url:").data(ticket.getUrls().getBusfor()));
                if (urls.getInfobus() != null)
                    emitter.send(SseEmitter.event().name("Infobus url:").data(ticket.getUrls().getInfobus()));
            }
        }

        emitter.complete();
    }


    @Async
    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void deleteOldTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now();

        routeRepository.deleteRouteByAddingTimeBefore(fifteenMinutesAgo);
    }

}
