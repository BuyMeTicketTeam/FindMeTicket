package com.booking.app.repositories;

import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusTicketRepository extends JpaRepository<BusTicket, UUID> {

    List<BusTicket> findByRoute(Route route);
}
