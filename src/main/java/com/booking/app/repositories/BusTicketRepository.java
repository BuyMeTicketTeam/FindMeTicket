package com.booking.app.repositories;

import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusTicketRepository extends JpaRepository<BusTicket, UUID> {

    Optional<BusTicket> findByDepartureTimeAndArrivalDateTimeAndCarrier(LocalTime departureTime, Instant arrivalDateTime, String carrier);

    Optional<List<BusTicket>> findByRoute(Route route);

}
