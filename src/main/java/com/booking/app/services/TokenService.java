package com.booking.app.services;

/**
 * Service interface for handling confirmation tokens and user verification.
 */
public interface TokenService {

    /**
     * Verifies a given token for a specific user.
     *
     * @param email      The email associated with the user.
     * @param givenToken The token to be verified.
     * @return boolean Returns true if the token is valid and not expired, false otherwise.
     */
    boolean verifyToken(String email, String givenToken);

}


