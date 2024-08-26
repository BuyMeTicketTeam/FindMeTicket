package com.booking.app.controllers.auth;

import com.booking.app.constants.ContentLanguage;
import com.booking.app.dto.users.RegistrationDTO;
import com.booking.app.entities.user.ConfirmationCode;
import com.booking.app.entities.user.User;
import com.booking.app.mappers.UserMapper;
import com.booking.app.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@DisplayName("Registration Controller API tests")
public class RegistrationControllerTest {

    private static final String URL = "/auth/sign-up";
    private static final String ENG = ContentLanguage.ENG.getLanguage();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final String rawPassword = "RawPassword333";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeAll
    public void setup() {
        userRepository.deleteAll();
        createAndSaveUser();
    }

    private void createAndSaveUser() {
        ConfirmationCode confirmationCode = ConfirmationCode.createCode();
        testUser = userRepository.save(User.createBasicUser(
                confirmationCode,
                "present-user@gmail.com",
                passwordEncoder.encode(rawPassword),
                "ExistedUser333",
                true)
        );
    }

    @Nested
    @DisplayName("POST register a user")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Post {
        private RegistrationDTO requestBody;

        private static Stream<Arguments> invalidRegistrationDtoArgs() {
            return Stream.of(
                    Arguments.of(null, "Valid-username", "ValidPassword777", "ValidPassword777", true, 400, "Empty email"),
                    Arguments.of("valid-email@gmail.com", null, "ValidPassword777", "ValidPassword777", true, 400, "Empty username"),
                    Arguments.of("valid-email@gmail.com", "Valid-username", null, "ValidPassword777", true, 400, "Empty password"),
                    Arguments.of("valid-email@gmail.com", "Valid-username", "ValidPassword777", null, true, 400, "Empty confirm password"),
                    Arguments.of("Invalid-email", "Valid-username", "ValidPassword777", "ValidPassword777", true, 400, "This is not a valid email format"),
                    Arguments.of("valid-email@gmail.com", "non", "ValidPassword777", "ValidPassword777", true, 400, "Username must be between 3 and 30 characters"),
                    Arguments.of("valid-email@gmail.com", "valid-username", "short", "short", true, 400, "Password must be of 8 - 30 characters and contain at least one letter and one number"),
                    Arguments.of("valid-email@gmail.com", "valid-username", "ValidPassword777", "DifferentPassword777", true, 400, "Passwords do not match")
            );

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sign new user up")
        void signUpUserIs200() {
            mvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestBody))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @BeforeAll
        void beforeAll() {
            requestBody = RegistrationDTO.builder()
                    .email("test-email@gmail.com")
                    .username("test-username")
                    .password("TestPassword777")
                    .confirmPassword("TestPassword777")
                    .notification(true)
                    .build();
        }

        @Test
        @SneakyThrows
        @DisplayName("[400] request body missed")
        void signUpUserBodyMissedIs400() {
            mvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] user already exists")
        void signUpUserAlreadyExistsIs200() {
            RegistrationDTO body = userMapper.toRegistrationDto(testUser);
            body.setPassword(rawPassword);
            body.setConfirmPassword(rawPassword);

            mvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(body))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "With email: {0}, username: {1}, password: {2}, confirmPassword: {3}, notification: {4} => status {5}")
        @MethodSource("invalidRegistrationDtoArgs")
        @SneakyThrows
        @DisplayName("[400] validation errors")
        void signUpUserWithValidationErrorsIs400(
                String email,
                String username,
                String password,
                String confirmPassword,
                boolean notification,
                int expectedStatus,
                String expectedErrorMessage
        ) {
            requestBody = RegistrationDTO.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .confirmPassword(confirmPassword)
                    .notification(notification)
                    .build();

            mvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestBody))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().is(expectedStatus))
                    .andExpect(jsonPath("$.message").value(expectedErrorMessage));
        }

    }

}
