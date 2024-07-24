package com.booking.app.services.impl;

import com.booking.app.repositories.RouteRepository;
import com.booking.app.services.PopularRoutesService;
import com.booking.app.services.ScheduledTicketDeletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Profile(value = "!test")
public class ScheduledTicketDeletionServiceImpl implements ScheduledTicketDeletionService {

    private final RouteRepository routeRepository;

    private final PopularRoutesService popularRoutesService;

    @Override
    @Async
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 30)
    @Transactional
    public void deleteOldTickets() throws IOException, ParseException {
        Instant fifteenMinutesAgo = Instant.now().minusSeconds(15 * 30);

        routeRepository.deleteRouteByAddingTimeBefore(fifteenMinutesAgo);

        popularRoutesService.findRoutes();
    }

}
