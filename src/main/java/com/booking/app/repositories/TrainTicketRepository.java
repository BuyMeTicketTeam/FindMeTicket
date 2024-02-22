package com.booking.app.repositories;

import com.booking.app.entity.Route;
import com.booking.app.entity.TrainTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainTicketRepository extends JpaRepository<TrainTicket, UUID> {
    Optional<List<TrainTicket>> findByRoute(Route route);

}