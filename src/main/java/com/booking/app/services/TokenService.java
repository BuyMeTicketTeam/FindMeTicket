package com.booking.app.services;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;

/**
 * Service interface for handling confirmation tokens and user verification.
 */
public interface TokenService {

    /**
     * Verifies a given token for a specific user.
     *
     * @param email The email associated with the user.
     * @param givenToken The token to be verified.
     * @return boolean Returns true if the token is valid and not expired, false otherwise.
     */
    public boolean verifyToken(String email, String givenToken);

    /**
     * Creates a confirmation token for the given user.
     *
     * @param user The user for whom the confirmation token is created.
     * @return ConfirmToken An object containing the user, expiry time, and a randomly generated token.
     */
    public ConfirmToken createConfirmToken(User user);

    /**
     * Generates a random token consisting of characters from a predefined set.
     *
     * @return String A randomly generated token.
     */
    public String generateRandomToken();
}


