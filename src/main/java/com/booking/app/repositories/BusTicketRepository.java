package com.booking.app.repositories;

import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusTicketRepository extends JpaRepository<BusTicket, UUID> {

    Optional<BusTicket> findByDepartureTimeAndArrivalTimeAndArrivalDateAndCarrier(String departureTime, String arrivalTime, String arrivalDate, String carrier);

    Optional<List<BusTicket>> findByRoute(Route route);

}
