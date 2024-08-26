package com.booking.app.controllers.auth;

import com.booking.app.entities.user.ConfirmationCode;
import com.booking.app.entities.user.User;
import com.booking.app.repositories.UserRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.servlet.http.Cookie;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.booking.app.constants.AuthenticatedUserConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@DisplayName("Logout Controller API tests")
public class LogoutControllerTest {

    private static final String URL = "/auth/sign-out";
    private static final String COOKIE_EXPIRED = "Max-Age=0";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        userRepository.deleteAll();
        createAndSaveUser();
    }

    private void createAndSaveUser() {
        ConfirmationCode confirmationCode = ConfirmationCode.createCode();

        userRepository.save(User.createBasicUser(
                confirmationCode,
                "present-user@gmail.com",
                "Password333",
                "ExistedUser333",
                true)
        );
    }

    @Nested
    @DisplayName("GET user logout")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Get {

        @Test
        @SneakyThrows
        @DisplayName("[200] logout")
        void userLogoutIs200() {
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, "some-refresh-token");
            Cookie userIdCookie = new Cookie(USER_ID, "some-user-id");
            Cookie rememberMeCookie = new Cookie(REMEMBER_ME, "some-remember-me-token");

            MockHttpServletResponse response = mvc.perform(get(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .cookie(refreshTokenCookie, userIdCookie, rememberMeCookie)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            List<String> cookies = response.getHeaders(HttpHeaders.SET_COOKIE);

            assertTrue(cookies.stream().anyMatch(cookie ->
                            cookie.startsWith(REFRESH_TOKEN) &&
                                    cookie.contains(COOKIE_EXPIRED)
                    ),
                    "REFRESH_TOKEN cookie was not deleted."
            );

            assertTrue(cookies.stream().anyMatch(cookie ->
                            cookie.startsWith(REMEMBER_ME) &&
                                    cookie.contains(COOKIE_EXPIRED)
                    ),
                    "REMEMBER_ME cookie was not deleted."
            );

            assertTrue(cookies.stream().anyMatch(cookie ->
                            cookie.startsWith(USER_ID) &&
                                    cookie.contains(COOKIE_EXPIRED)
                    ),
                    "USER_ID cookie was not deleted."
            );

        }

    }

}
