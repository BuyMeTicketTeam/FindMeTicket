package com.booking.app.repositories;

import com.booking.app.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {


    @Modifying
    @Query(value = "UPDATE Ticket u SET u.url = :url WHERE u.id = :ticketId")
    void changeUrlById(@Param("ticketId") UUID ticketId, @Param("url") String url);
}
