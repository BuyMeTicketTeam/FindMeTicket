package com.booking.app.repositories;

import com.booking.app.entity.UkrainianPlaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface UkrPlacesRepository extends JpaRepository<UkrainianPlaces, BigInteger> {
    Optional<List<UkrainianPlaces>> findUkrainianPlacesByNameUaStartingWithIgnoreCase(String startLetters);
    Optional<List<UkrainianPlaces>> findUkrainianPlacesByNameEngStartingWithIgnoreCase(String startLetters);
}
