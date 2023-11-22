package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.EmailExistsException;
import com.booking.app.exception.exception.UsernameExistsException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.TokenService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceImplTest {
    @InjectMocks
    private UserSecurityServiceImpl userSecurityService;

    @Mock
    private UserSecurityRepository userSecurityRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private VerifyEmailRepository verifyEmailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @Mock
    private MailSenderService mailService;

    @Mock
    private TokenService tokenService;

    private UserSecurity userSecurity;
    private ConfirmToken confirmToken;
    private User user;
    private RegistrationDTO registrationDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        registrationDTO = RegistrationDTO.builder().email("mishaakamichael999@gmail.com")
                .username("Michael999")
                .password("GloryToUkraine5")
                .confirmPassword("GloryToUkraine5").build();
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void testSuccessfullyRegistration() throws MessagingException {
        UserSecurityServiceImpl temp = Mockito.spy(userSecurityService);
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername())).thenReturn(Optional.empty());
        doReturn(new EmailDTO(registrationDTO.getEmail())).when(temp).performRegistration(registrationDTO);
        EmailDTO newUser = temp.register(registrationDTO);
        assertEquals(registrationDTO.getEmail(), newUser.getEmail());
    }
    //CПРАВЕДБЫДЛО

    @Test
    void testEmailExistsExceptionRegistration() throws MessagingException {
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername()))
                .thenReturn(Optional.of(UserSecurity.builder().email(registrationDTO.getEmail()).username("Lalka228").enabled(true).build()));

        assertThrows(EmailExistsException.class, () -> userSecurityService.register(registrationDTO));
    }

    @Test
    void testUsernameExistsExceptionRegistration() throws MessagingException {
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername()))
                .thenReturn(Optional.of(UserSecurity.builder().email("rafaello2@gmail.com").username(registrationDTO.getUsername()).enabled(true).build()));

        assertThrows(UsernameExistsException.class, () -> userSecurityService.register(registrationDTO));
    }

    @Test
    void testRegistrationDeletesUnconfirmedUser() throws MessagingException {
        UserSecurityServiceImpl temp = Mockito.spy(userSecurityService);
        UserSecurity userSecurity = UserSecurity.builder().email("hasbulla@gmail.com").username("hasbulla23").build();
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername())).thenReturn(Optional.of(userSecurity));
        doNothing().when(temp).deleteUserIfNotConfirmed(userSecurity);
        doReturn(new EmailDTO(registrationDTO.getEmail())).when(temp).performRegistration(registrationDTO);
        EmailDTO newUser = temp.register(registrationDTO);
        assertEquals(registrationDTO.getEmail(), newUser.getEmail());
    }

    @Test
    void performRegistration() {
    }

    @Test
    void createNewRegisteredUser() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void enableIfValid() {
    }
}