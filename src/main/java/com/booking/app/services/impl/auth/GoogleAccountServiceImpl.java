package com.booking.app.services.impl.auth;

import com.booking.app.dto.users.SocialLoginDto;
import com.booking.app.entities.user.AuthProvider;
import com.booking.app.entities.user.Role;
import com.booking.app.entities.user.User;
import com.booking.app.properties.ApiProps;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.AuthProviderService;
import com.booking.app.services.GoogleAccountService;
import com.booking.app.services.RoleService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Service implementation for Google Account-related operations.
 * <p>
 * This service provides functionality for handling Google OAuth login and user creation or update.
 */
@Service
public class GoogleAccountServiceImpl implements GoogleAccountService {

    private final RoleService roleService;
    private final AuthProviderService authProviderService;

    private final UserRepository userRepository;

    private final String clientId;

    @Autowired
    public GoogleAccountServiceImpl(RoleService roleService, AuthProviderService authProviderService, UserRepository userRepository, ApiProps apiProps) {
        this.roleService = roleService;
        this.authProviderService = authProviderService;
        this.userRepository = userRepository;
        this.clientId = apiProps.getGoogleClientId();
    }

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    void setGoogleIdTokenVerifier() {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    /**
     * Login a user using Google OAuth and create or update the user
     *
     * @param requestBody DTO containing the Google OAuth2 ID Token.
     * @return User for the logged-in user.
     */
    public Optional<User> login(SocialLoginDto requestBody) {
        try {
            GoogleIdToken account = verifier.verify(requestBody.getIdToken());
            return Optional.of(createOrUpdateUser(account));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Create or update a user based on the information in the provided Google ID Token.
     *
     * @param googleIdToken Google ID Token containing user information.
     * @return User for the created or updated user.
     */
    private User createOrUpdateUser(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        User existingAccount = userRepository.findByEmail(payload.getEmail()).orElse(null);

        if (existingAccount == null) {
            Role role = roleService.findByType(Role.RoleType.USER);
            AuthProvider provider = authProviderService.findByType(AuthProvider.AuthProviderType.GOOGLE);
            User user = User.createGoogleUser(
                    provider,
                    role,
                    payload.get("given_name") + " " + payload.get("family_name"),
                    payload.getEmail(),
                    (String) payload.get("picture")
            );
            return userRepository.save(user);
        } else {
            existingAccount.setUsername(payload.get("given_name") + " " + payload.get("family_name"));
            existingAccount.setSocialMediaAvatar((String) payload.get("picture"));
            userRepository.save(existingAccount);
            return existingAccount;
        }
    }

}
