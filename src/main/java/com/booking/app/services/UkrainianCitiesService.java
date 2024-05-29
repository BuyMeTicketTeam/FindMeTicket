package com.booking.app.services;


public interface UkrainianCitiesService {

    /**
     * Retrieves the name of a city based on its ID and language.
     *
     * @param id       The ID of the city.
     * @param language The language for the city name.
     * @return The name of the city.
     */
    String getCity(Long id, String language);

}
