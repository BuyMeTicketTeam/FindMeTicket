package com.booking.app.services.impl;

import com.booking.app.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketScheduledDeleteService {

    private final RouteRepository routeRepository;

    @Async
    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void deleteOldTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now();

        routeRepository.deleteRouteByAddingTimeBefore(fifteenMinutesAgo);
    }
}
