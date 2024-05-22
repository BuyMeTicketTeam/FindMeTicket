package com.booking.app.services.impl;

import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.ForbiddenDeleteUserException;
import com.booking.app.exception.exception.UserNotFoundException;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.security.filter.JwtProvider;
import com.booking.app.services.DeleteUserService;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.booking.app.constant.CustomHttpHeaders.REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Log4j2
public class DeleteUserServiceImpl implements DeleteUserService {

    private final UserCredentialsRepository userCredentialsRepository;

    private final JwtProvider jwtProvider;

    @Override
    public ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = JwtProvider.extractRefreshToken(request);
        String email = extractEmail(refreshToken);

        log.info("User with email: {} is about to delete account", email);
        UserCredentials user = getUserByEmail(email);

        if (doIdsMatch(getIdFromUserId(request), user.getId())) {
            deleteUser(user, request, response);
            return ResponseEntity.ok("User's been deleted successfully");
        } else {
            logUnsecuredDeleteAttempt();
            throw new ForbiddenDeleteUserException("Data in cookies (USER_ID, Tokens) don't match with one another");
        }
    }

    /**
     * Extracts the email from the refresh token.
     *
     * @param refreshToken the refresh token
     * @return the email extracted from the token
     * @throws ForbiddenDeleteUserException if the email cannot be extracted
     */
    private String extractEmail(String refreshToken) {
        if (refreshToken == null) {
            logUnsecuredDeleteAttempt();
            throw new ForbiddenDeleteUserException("Impossible to extract user from passed token");
        }
        String email = jwtProvider.extractEmail(refreshToken);
        if (email == null) {
            logUnsecuredDeleteAttempt();
            throw new ForbiddenDeleteUserException("No user has been found");
        }
        return email;
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the UserCredentials of the user
     * @throws UserNotFoundException if no user is found with the given email
     */
    private UserCredentials getUserByEmail(String email) {
        return userCredentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user present by email"));
    }

    /**
     * Retrieves the user ID from the cookies in the request.
     *
     * @param request the HTTP request containing cookies
     * @return the user ID
     * @throws UserNotFoundException if no user ID is found in the cookies
     */
    private UUID getIdFromUserId(HttpServletRequest request) {
        return UUID.fromString(CookieUtils.getCookie(request, USER_ID)
                .orElseThrow(() -> new UserNotFoundException("No user present by ID")).getValue());
    }

    /**
     * Checks if the user's ID from USER_ID Cookie and user's identity from REFRESH_TOKEN Cookie match
     *
     * @param idFromUserId       the user ID from USER_ID
     * @param idFromRefreshToken the user ID from REFRESH_TOKEN
     * @return true if they match, false otherwise
     */
    private boolean doIdsMatch(UUID idFromUserId, UUID idFromRefreshToken) {
        return idFromUserId.equals(idFromRefreshToken);
    }

    /**
     * Deletes the user and their associated cookies.
     *
     * @param user     the UserCredentials of the user
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    private void deleteUser(UserCredentials user, HttpServletRequest request, HttpServletResponse response) {
        userCredentialsRepository.delete(user);
        deleteCookies(request, response);
    }

    /**
     * Logs an unsecured attempt to delete a user.
     */
    private void logUnsecuredDeleteAttempt() {
        log.info("Unsecured attempt to delete user");
    }

    /**
     * Deletes the relevant cookies from the request and response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    private static void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.deleteCookie(request, response, USER_ID);
        CookieUtils.deleteCookie(request, response, REMEMBER_ME);
    }

}
