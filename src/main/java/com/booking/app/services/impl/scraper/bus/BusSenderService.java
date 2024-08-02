package com.booking.app.services.impl.scraper.bus;

import com.booking.app.dto.tickets.UrlAndPriceDTO;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.mappers.BusMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.services.ScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.booking.app.constants.SiteConstants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class BusSenderService {

    private final BusMapper busMapper;
    private final BusTicketRepository busTicketRepository;
    @Qualifier("busforBusService")
    private final ScraperService busforBusService;

    @Qualifier("infobusBusService")
    private final ScraperService infobusBusService;

    @Qualifier("proizdBusService")
    private final ScraperService proizdBusService;

    public CompletableFuture<Boolean> sendTicket(BusTicket busTicket, SseEmitter emitter, String language, MutableBoolean emitterExpiration) throws IOException {
        emitter.send(SseEmitter.event().name("Ticket info").data(busMapper.ticketToTicketDto(busTicket, language)));

        if (!busTicket.linksPresent()) {
            CompletableFuture<Void> allOf = scrapeBusLink(busTicket, emitter, language, emitterExpiration);

            try {
                allOf.join();
            } catch (CancellationException | CompletionException e) {
                log.error("Error in Scraper service, scrapeTickets() method: " + e.getMessage());
                emitter.completeWithError(e);
                return CompletableFuture.completedFuture(false);
            }
            busTicketRepository.save(busTicket);
        } else {
            busTicket.getInfoList().forEach(t -> {
                try {
                    emitter.send(SseEmitter.event().name(t.getSourceWebsite()).data(
                            UrlAndPriceDTO.builder()
                                    .price(t.getPrice())
                                    .url(t.getLink())
                                    .build())
                    );
                    log.info("Bus ticket from {} instantly returned by link: {}", t.getSourceWebsite(), t.getLink());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        return CompletableFuture.completedFuture(true);
    }

    private CompletableFuture<Void> scrapeBusLink(BusTicket busTicket, SseEmitter emitter, String language, MutableBoolean emitterExpiration) {
        List<CompletableFuture<Boolean>> completableFutureListBus = new LinkedList<>();

        busTicket.getInfoList().forEach(t -> {
            try {
                switch (t.getSourceWebsite()) {
                    case PROIZD_UA ->
                            completableFutureListBus.add(proizdBusService.scrapeTicketUri(emitter, busTicket, language, emitterExpiration));
                    case BUSFOR_UA ->
                            completableFutureListBus.add(busforBusService.scrapeTicketUri(emitter, busTicket, language, emitterExpiration));
                    case INFOBUS ->
                            completableFutureListBus.add(infobusBusService.scrapeTicketUri(emitter, busTicket, language, emitterExpiration));
                    default -> throw new IllegalArgumentException();
                }
            } catch (IOException | ParseException exception) {
                log.error(exception.getMessage());
            }
        });

        return CompletableFuture.allOf(completableFutureListBus.toArray((CompletableFuture[]::new)));
    }

}
