package com.booking.app.repositories;

import com.booking.app.entity.Ticket;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByDepartureCityAndArrivalCityAndDepartureDate(String departureCity, String arrivalCity, String departureDate);

    List<Ticket> findByAddingTime(LocalDateTime dateTime);

    @Modifying
    @Transactional
    @Query("DELETE FROM Ticket e WHERE e.addingTime < :thresholdDateTime")
    void deleteOlderThan(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);
}
