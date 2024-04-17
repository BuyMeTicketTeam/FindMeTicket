package com.booking.app.repositories;

import com.booking.app.entity.ticket.Route;
import com.booking.app.entity.ticket.train.TrainTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainTicketRepository extends JpaRepository<TrainTicket, UUID> {
    Optional<List<TrainTicket>> findByRoute(Route route);

    Optional<TrainTicket> findByDepartureTimeAndArrivalTimeAndArrivalDateAndCarrier(String departureTime, String arrivalTime, String arrivalDate, String carrier);
}