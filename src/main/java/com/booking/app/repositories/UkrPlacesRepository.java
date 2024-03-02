package com.booking.app.repositories;

import com.booking.app.entity.UkrainianPlaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UkrPlacesRepository extends JpaRepository<UkrainianPlaces, Long> {

    Optional<List<UkrainianPlaces>> findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContainingIgnoreCase(String startLetters, String exclusion);

    Optional<List<UkrainianPlaces>> findUkrainianPlacesByNameUaStartsWithIgnoreCaseAndNameUaNotContainingIgnoreCase(String startLetters, String exclusion);

}
