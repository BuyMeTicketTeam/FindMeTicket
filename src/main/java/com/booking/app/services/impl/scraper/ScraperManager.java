package com.booking.app.services.impl.scraper;

import com.booking.app.dto.tickets.RequestTicketsDto;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.Ticket;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.entities.ticket.train.TrainTicket;
import com.booking.app.exceptions.notfound.TicketNotFoundException;
import com.booking.app.mappers.BusMapper;
import com.booking.app.mappers.RouteMapper;
import com.booking.app.mappers.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import com.booking.app.services.ScraperService;
import com.booking.app.services.impl.scraper.bus.BusSenderService;
import com.booking.app.services.impl.scraper.train.TrainSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
@RequiredArgsConstructor
public class ScraperManager {

    @Qualifier("busforBusService")
    private final ScraperService busforBusService;

    @Qualifier("infobusBusService")
    private final ScraperService infobusBusService;

    @Qualifier("proizdBusService")
    private final ScraperService proizdBusService;

    @Qualifier("proizdTrainService")
    private final ScraperService proizdTrainService;

    @Qualifier("gdtickets")
    private final ScraperService gdticketsBusService;

    private final RouteRepository routeRepository;

    private final BusTicketRepository busTicketRepository;

    private final TicketRepository ticketRepository;

    private final BusMapper busMapper;

    private final TrainMapper trainMapper;

    private final RouteMapper routeMapper;

    private final BusSenderService busSenderService;
    private final TrainSenderService trainSenderService;

    /**
     * Finds tickets based on the provided request.
     *
     * @param requestTicketDTO the request data for the tickets
     * @param emitter          the SSE emitter to send the ticket data
     * @param language         the language in which to return the ticket information
     * @return a CompletableFuture representing the completion status of the operation
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if a parsing error occurs
     */
    @Async
    public CompletableFuture<Boolean> findTickets(RequestTicketsDto requestTicketDTO, SseEmitter emitter, MutableBoolean emitterNotExpired, String language) throws IOException, ParseException {
        CompletableFuture<Boolean> result = sendTickets(requestTicketDTO, emitter, emitterNotExpired, language);
        if (emitter != null) emitter.complete();
        return result;
    }

    /**
     * Retrieves ticket information based on the provided ticket ID.
     *
     * @param id       the ticket ID
     * @param emitter  the SSE emitter to send the ticket data
     * @param language the language in which to return the ticket information
     * @return a CompletableFuture representing the completion status of the operation
     * @throws IOException if an I/O error occurs
     */
    @Async
    public CompletableFuture<Boolean> getTicketInfo(UUID id, SseEmitter emitter, MutableBoolean emitterNotExpired, String language) throws IOException {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new TicketNotFoundException(id.toString()));
        } catch (TicketNotFoundException exception) {
            log.warn(exception.getMessage());
            return CompletableFuture.completedFuture(false);
        }

        switch (ticket) {
            case BusTicket busTicket -> busSenderService.sendTicket(busTicket, emitter, language, emitterNotExpired);
            case TrainTicket trainTicket -> trainSenderService.sendTicket(trainTicket, emitter, language);
            default -> {
                emitter.complete();
                return CompletableFuture.completedFuture(false);
            }
        }
        emitter.complete();
        return CompletableFuture.completedFuture(true);
    }

    private CompletableFuture<Boolean> sendTickets(RequestTicketsDto requestBody, SseEmitter emitter, MutableBoolean emitterNotExpired, String language) throws ParseException, IOException {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(
                requestBody.getDepartureCity(),
                requestBody.getArrivalCity(),
                requestBody.getDepartureDate()
        );

        if (route == null) {
            return scrapeTickets(requestBody, emitter, language, emitterNotExpired);
        } else {
            return extractTickets(requestBody, emitter, language, route, emitterNotExpired);
        }
    }

    private CompletableFuture<Boolean> scrapeTickets(RequestTicketsDto requestTicketDTO, SseEmitter emitter, String language, MutableBoolean emitterNotExpired) throws ParseException, IOException {
        Route newRoute = routeMapper.toRoute(requestTicketDTO);
        routeRepository.save(newRoute);

        List<CompletableFuture<Boolean>> completableFutureList = ticketsServices(emitter, newRoute, language,
                requestTicketDTO.getBus(), requestTicketDTO.getTrain(), emitterNotExpired);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureList.toArray(CompletableFuture[]::new));

        try {
            allOf.join();
        } catch (CancellationException | CompletionException e) {
            log.error("Error in Scraper service, scrapeTickets() method: {}", e.getMessage());
            if (emitter != null) emitter.completeWithError(e);
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.completedFuture(completableFutureList.stream().map(services -> {
            try {
                return services.get();
            } catch (InterruptedException e) {
                log.error("Error in Scraper service, some ticket service has failed: {}", e.getMessage());
                throw new CompletionException(e);
            } catch (ExecutionException e) {
                log.error("Error in Scraper service, some ticket service has failed: {}", e.getMessage());
                throw new CompletionException(e);
            }
        }).filter(service -> !service).findFirst().isEmpty());
    }

    private CompletableFuture<Boolean> extractTickets(RequestTicketsDto requestTicketDTO, SseEmitter emitter, String language, Route route, MutableBoolean emitterNotExpired) throws IOException {
        List<Ticket> tickets = ticketRepository.findByRouteId(route.getId());
        if (!tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                if (emitterNotExpired.booleanValue()) {
                    if (requestTicketDTO.getBus() && ticket instanceof BusTicket busTicket) {
                        emitter.send(SseEmitter.event().data(busMapper.ticketToTicketDto(busTicket, language)));
                    } else if (requestTicketDTO.getTrain() && ticket instanceof TrainTicket trainTicket) {
                        emitter.send(SseEmitter.event().data(trainMapper.toTrainTicketDto(trainTicket, language)));
                    }
                }
            }
            return CompletableFuture.completedFuture(true);
        } else {
            return CompletableFuture.completedFuture(false);
        }
    }

    private List<CompletableFuture<Boolean>> ticketsServices(SseEmitter emitter, Route newRoute, String
            language, Boolean doSendBusTicket, Boolean doSendTrainTicket, MutableBoolean emitterNotExpired) throws ParseException, IOException {
        return Arrays.asList(
                infobusBusService.scrapeTickets(emitter, newRoute, language, doSendBusTicket, emitterNotExpired),
                proizdBusService.scrapeTickets(emitter, newRoute, language, doSendBusTicket, emitterNotExpired),
                busforBusService.scrapeTickets(emitter, newRoute, language, doSendBusTicket, emitterNotExpired),
//                  gdticketsBusService.scrapeTickets(emitter, newRoute, language, doSendBusTicket)
                proizdTrainService.scrapeTickets(emitter, newRoute, language, doSendTrainTicket, emitterNotExpired)
        );
    }

}
