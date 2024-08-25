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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("Registration Controller API tests")
public class RegistrationControllerTest {

    private static final String URL = "/auth/sign-up";
    private static final String ENG = ContentLanguage.ENG.getLanguage();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private MockMvc mvc;

    private RegistrationDTO presentUser;


    @BeforeAll
    public void setup() {
        userRepository.deleteAll();
        saveUser();

        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    private void saveUser() {
        ConfirmationCode confirmationCode = ConfirmationCode.createCode();
        String password = "Password333";
        presentUser = userMapper.toRegistrationDto(userRepository.save(User.createBasicUser(
                confirmationCode,
                "present-user@gmail.com",
                password,
                "ExistedUser333",
                true)
        ));
        presentUser.setConfirmPassword(password);
    }

    @Nested
    @DisplayName("POST register a user")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Post {
        private RegistrationDTO requestBody;

        private static Stream<Arguments> getErrorValidatedRegistrationDtoArgs() {
            return Stream.of(
                    Arguments.of("Invalid-email", "Valid-username", "ValidPassword777", "ValidPassword777", true, 400),
                    Arguments.of("valid-email@gmail.com", "non", "ValidPassword777", "ValidPassword777", true, 400),
                    Arguments.of("valid-email@gmail.com", "valid-username", "invalid", "invalid", true, 400),
                    Arguments.of("valid-email@gmail.com", "valid-username", "ValidPassword777", "DifferentPassword777", true, 400)
            );
        }

        @BeforeEach
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

        @Test
        @SneakyThrows
        @DisplayName("[200] user already exists")
        void signUpUserAlreadyExistsIs200() {
            mvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(presentUser))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
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

        @ParameterizedTest(name = "With email: {0}, username: {1}, password: {2}, confirmPassword: {3}, notification: {4} => status {5}")
        @MethodSource("getErrorValidatedRegistrationDtoArgs")
        @SneakyThrows
        @DisplayName("[400] validation errors")
        void signUpUserWithValidationErrorsIs400(
                String email,
                String username,
                String password,
                String confirmPassword,
                boolean notification,
                int expectedStatus
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
                    .andExpect(status().is(expectedStatus));
        }

    }
}
