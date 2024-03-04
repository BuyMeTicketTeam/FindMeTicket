package com.booking.app.services.impl;

import com.booking.app.repositories.RouteRepository;
import com.booking.app.services.DeleteTicketService;
import com.booking.app.services.PopularRoutesService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ScheduledTicketDeleteService implements DeleteTicketService {

    private final RouteRepository routeRepository;

    private final PopularRoutesService popularRoutesService;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Async
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 60)
    @Transactional
    public void deleteOldTickets() throws IOException, ParseException {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now();
        routeRepository.deleteRouteByAddingTimeBefore(fifteenMinutesAgo);

        popularRoutesService.findRoutes();
    }

}
