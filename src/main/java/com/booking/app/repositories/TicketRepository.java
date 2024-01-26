package com.booking.app.repositories;

import com.booking.app.entity.Ticket;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {




    void deleteById(UUID id);

    void deleteByRouteId(UUID id);

}
