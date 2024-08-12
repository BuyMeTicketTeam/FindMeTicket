package com.booking.app.controllers;

import com.booking.app.constants.ContentLanguage;
import com.booking.app.dto.users.RegistrationDTO;
import com.booking.app.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

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
    private static final String UA = ContentLanguage.UA.getLanguage();
    private static final String ENG = ContentLanguage.ENG.getLanguage();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;

    @BeforeAll
    public void setup() {
//        routeRepository.deleteAll();
//        initData();

        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Nested
    @DisplayName("POST register a user")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Post {
        private RegistrationDTO requestBody;

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
        @DisplayName("[200] sign new user up")
        void signUpNewUserIs200() {
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

    }
}
