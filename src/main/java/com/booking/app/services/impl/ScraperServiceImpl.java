package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.UrlAndPriceDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TrainTicket;
import com.booking.app.exception.exception.ResourceNotFoundException;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static com.booking.app.constant.SiteConstants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScraperServiceImpl {

    private final ScraperService busfor;

    private final ScraperService infobus;

    private final ScraperService proizd;

    private final ScraperService train;

    private final RouteRepository routeRepository;

    private final BusTicketRepository busTicketRepository;

    private final BusMapper busMapper;

    private final TrainMapper trainMapper;

    @Async
    public CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, String language) throws IOException, ParseException {

        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());
        Route newRoute = null;
        if (route == null) {
            newRoute = createRoute(requestTicketDTO);

            List<CompletableFuture<Boolean>> completableFutureListBus = completableFutureListBuses(emitter, newRoute, language);
            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureListBus.toArray(CompletableFuture[]::new));
            try {
                allOf.join();
            } catch (CancellationException | CompletionException e) {
                log.error("Error in Scraper service, scrapeTickets() method: " + e.getMessage());
                emitter.completeWithError(e);
                return CompletableFuture.completedFuture(false);
            }
            if (completableFutureListBus.stream().anyMatch(t -> {
                try {
                    return t.get().equals(true);
                } catch (ExecutionException | InterruptedException e) {
                    log.warn("Interrupted while waiting for CompletableFuture result.", e);
                    /* Clean up whatever needs to be handled before interrupting  */
                    Thread.currentThread().interrupt();
                    return false;
                }
            })) {
                routeRepository.save(newRoute);
            }
            emitter.complete();

        } else {
            for (Ticket ticket : route.getTickets()) {
                if (ticket instanceof BusTicket && requestTicketDTO.getBus()) {
                    emitter.send(SseEmitter.event().name("ticket data: ").data(busMapper.ticketToTicketDto((BusTicket) ticket, language)));
                }
                if (ticket instanceof TrainTicket && requestTicketDTO.getTrain()) {
                    emitter.send(SseEmitter.event().name("ticket data: ").data(trainMapper.toTrainTicketDto((TrainTicket) ticket, language)));
                }
            }
        }

        emitter.complete();

        return route == null && newRoute.getTickets().isEmpty() ?
                CompletableFuture.completedFuture(false) : CompletableFuture.completedFuture(true);
    }

    @Async
    public void getTicket(UUID id, SseEmitter emitter, String language) throws IOException, ParseException {
        BusTicket ticket;
        try {
            ticket = busTicketRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(String.format("No ticket present by %s ID", id)));
        } catch (ResourceNotFoundException e) {
            emitter.completeWithError(e);
            return;
        }

        emitter.send(SseEmitter.event().name("ticket info").data(busMapper.ticketToTicketDto(ticket, language)));

        if (!ticket.linksAreScraped()) {

            List<CompletableFuture<Boolean>> completableFutureListBus = new LinkedList<>();

            if (ticket.getBusforPrice() != null)
                completableFutureListBus.add(busfor.getBusTicket(emitter, ticket, language));
            if (ticket.getInfobusPrice() != null)
                completableFutureListBus.add(infobus.getBusTicket(emitter, ticket, language));
            if (ticket.getProizdPrice() != null)
                completableFutureListBus.add(proizd.getBusTicket(emitter, ticket, language));

            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureListBus.toArray((CompletableFuture[]::new)));

            try {
                allOf.join();
            } catch (CancellationException | CompletionException e) {
                log.error("Error in Scraper service, scrapeTickets() method: " + e.getMessage());
                emitter.completeWithError(e);
                return;
            }

            busTicketRepository.save(ticket);
        } else {
            if (ticket.getProizdLink() != null) {
                emitter.send(SseEmitter.event().name(PROIZD_UA).data(UrlAndPriceDTO.builder()
                        .price(ticket.getProizdPrice())
                        .url(ticket.getProizdLink())
                        .build()));
                log.info("PROIZD URL IN single getTicket() INSTANTLY:" + ticket.getProizdLink());
            }
            if (ticket.getBusforLink() != null) {
                emitter.send(SseEmitter.event().name(BUSFOR_UA).data(UrlAndPriceDTO.builder()
                        .price(ticket.getBusforPrice())
                        .url(ticket.getBusforLink())
                        .build()));
                log.info("BUSFOR URL IN single getTicket() INSTANTLY: " + ticket.getBusforLink());
            }
            if (ticket.getInfobusLink() != null) {
                emitter.send(SseEmitter.event().name(INFOBUS).data(
                        UrlAndPriceDTO.builder()
                                .price(ticket.getInfobusPrice())
                                .url(ticket.getInfobusLink())
                                .build()));
                log.info("INFOBUS URL IN single getTicket() INSTANTLY:  " + ticket.getInfobusLink());
            }

        }

        emitter.complete();
    }

    private static Route createRoute(RequestTicketsDTO requestTicketDTO) {
        return Route.builder()
                .addingTime(LocalDateTime.now())
                .departureCity(requestTicketDTO.getDepartureCity())
                .arrivalCity(requestTicketDTO.getArrivalCity())
                .departureDate(requestTicketDTO.getDepartureDate())
                .build();
    }

    private List<CompletableFuture<Boolean>> completableFutureListBuses(SseEmitter emitter, Route newRoute, String language) throws ParseException, IOException {
        return Arrays.asList(
                infobus.scrapeTickets(emitter, newRoute, language),
                proizd.scrapeTickets(emitter, newRoute, language),
                busfor.scrapeTickets(emitter, newRoute, language)
//                train.scrapeTickets(emitter, newRoute, language)
        );
    }

}
