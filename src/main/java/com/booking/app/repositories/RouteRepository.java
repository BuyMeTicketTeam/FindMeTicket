package com.booking.app.repositories;

import com.booking.app.entities.ticket.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {
    Route findByDepartureCityAndArrivalCityAndDepartureDate(String departureCity, String arrivalCity, LocalDate departureDate);

    Route findByAddingTime(Instant dateTime);

    @Modifying
    @Query("DELETE FROM Route e WHERE e.addingTime < :thresholdDateTime")
    void deleteOlderThan(@Param("thresholdDateTime") Instant thresholdDateTime);

    Route findFirstByAddingTime(Instant addingTime);

    void deleteRouteByAddingTimeBefore(Instant addingTime);
}
