package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TicketUrl;
import com.booking.app.exception.exception.ResourceNotFoundException;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import com.booking.app.services.ScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Log4j2
public class ScraperServiceImpl {

    private final ScraperService busfor;

    private final ScraperService infobus;

    private final ScraperService proizd;

    private final RouteRepository routeRepository;

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;


    @Async
    public void scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter) throws IOException, ParseException {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

        if (route == null) {
            Route newRoute = createRoute(requestTicketDTO);

            List<CompletableFuture<Boolean>> completableFutureListBus = completableFutureListBuses(requestTicketDTO, emitter, newRoute);
            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureListBus.toArray(CompletableFuture[]::new));
            try {
                allOf.join();
            } catch (Exception e) {
                log.error("Error in Scraper service: " + e.getMessage());
                emitter.complete();
                return;
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
        Ticket ticket = ticketRepository.findById(id)
                .orElseGet(() -> {
                    try {
                        throw new ResourceNotFoundException(String.format("No ticket present by %s ID", id));
                    } catch (ResourceNotFoundException e) {
                        emitter.completeWithError(e);
                        return null;
                    }
                });
        if (ticket == null) return;

        emitter.send(SseEmitter.event().name("ticket info").data(ticketMapper.toDto(ticket)));

        TicketUrl urls = ticket.getUrls();

        if (urls == null) {

            ticket.setUrls(new TicketUrl());
            ticket.getUrls().setTicket(ticket);
            List<CompletableFuture<Boolean>> completableFutureListBus = Arrays.asList(
                    busfor.getTicket(emitter, ticket),
                    infobus.getTicket(emitter, ticket),
                    proizd.getTicket(emitter, ticket)
            );
            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureListBus.toArray(new CompletableFuture[0]));
            while (!allOf.isDone())

                ticketRepository.save(ticket);
        } else {
            if (urls.getProizd() != null)
                emitter.send(SseEmitter.event().name("Proizd url:").data(ticket.getUrls().getProizd()));
            if (urls.getBusfor() != null)
                emitter.send(SseEmitter.event().name("Busfor url:").data(ticket.getUrls().getBusfor()));
            if (urls.getInfobus() != null)
                emitter.send(SseEmitter.event().name("Infobus url:").data(ticket.getUrls().getInfobus()));
        }

        emitter.complete();
    }

    private Route createRoute(RequestTicketsDTO requestTicketDTO) {
        return Route.builder()
                .addingTime(LocalDateTime.now())
                .departureCity(requestTicketDTO.getDepartureCity())
                .arrivalCity(requestTicketDTO.getArrivalCity())
                .departureDate(requestTicketDTO.getDepartureDate())
                .build();
    }

    private List<CompletableFuture<Boolean>> completableFutureListBuses(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route newRoute) throws ParseException, IOException {
        return Arrays.asList(
                infobus.scrapeTickets(requestTicketDTO, emitter, newRoute),
                proizd.scrapeTickets(requestTicketDTO, emitter, newRoute),
                busfor.scrapeTickets(requestTicketDTO, emitter, newRoute)
        );
    }

}
