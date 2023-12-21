package com.booking.app.repositories;

import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
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
public interface RouteRepository extends JpaRepository<Route, UUID> {
    Route findByDepartureCityAndArrivalCityAndDepartureDate(String departureCity, String arrivalCity, String departureDate);

    Route findByAddingTime(LocalDateTime dateTime);

    @Modifying
    @Transactional
    @Query("DELETE FROM Route e WHERE e.addingTime < :thresholdDateTime")
    void deleteOlderThan(@Param("thresholdDateTime") LocalDateTime thresholdDateTime);
}
