package com.booking.app.repositories;

import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.train.TrainTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainTicketRepository extends JpaRepository<TrainTicket, UUID> {
    Optional<List<TrainTicket>> findByRoute(Route route);

    Optional<TrainTicket> findByDepartureTimeAndArrivalDateTimeAndCarrier(LocalTime departureTime, Instant arrivalDateTime, String carrier);
}