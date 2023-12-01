package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.entity.enums.EnumRole;
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

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private UserSecurityRepository userSecurityRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private VerifyEmailRepository verifyEmailRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailSenderService mailService;

    @Mock
    private TokenService tokenService;


    private UserSecurity userSecurity;
    private ConfirmToken confirmToken;
    private User user;
    private RegistrationDTO registrationDTO;
    private TokenConfirmationDTO tokenConfirmationDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        registrationDTO = RegistrationDTO.builder().email("mishaakamichael999@gmail.com")
                .username("Michael999")
                .password("GloryToUkraine5")
                .confirmPassword("GloryToUkraine5").build();
    }

    @Test
    void testSuccessfullyRegistration() throws MessagingException {
        RegistrationServiceImpl temp = Mockito.spy(registrationService);
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername())).thenReturn(Optional.empty());
        doReturn(new EmailDTO(registrationDTO.getEmail())).when(temp).performRegistration(registrationDTO);
        EmailDTO newUser = temp.register(registrationDTO);
        assertEquals(registrationDTO.getEmail(), newUser.getEmail());
    }

    @Test
    void testEmailExistsExceptionRegistration() throws MessagingException {
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername()))
                .thenReturn(Optional.of(UserSecurity.builder().email(registrationDTO.getEmail()).username("Lalka228").enabled(true).build()));

        assertThrows(EmailExistsException.class, () -> registrationService.register(registrationDTO));
    }

    @Test
    void testUsernameExistsExceptionRegistration() throws MessagingException {
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername()))
                .thenReturn(Optional.of(UserSecurity.builder().email("rafaello2@gmail.com").username(registrationDTO.getUsername()).enabled(true).build()));

        assertThrows(UsernameExistsException.class, () -> registrationService.register(registrationDTO));
    }

    @Test
    void testDeletesUnconfirmedUserRegistration() throws MessagingException {
        RegistrationServiceImpl temp = Mockito.spy(registrationService);
        UserSecurity userSecurity = UserSecurity.builder().email("hasbulla@gmail.com").username("hasbulla23").build();
        when(userSecurityRepository.findByEmailOrUsername(registrationDTO.getEmail(), registrationDTO.getUsername())).thenReturn(Optional.of(userSecurity));
        doNothing().when(temp).deleteUserIfNotConfirmed(userSecurity);
        doReturn(new EmailDTO(registrationDTO.getEmail())).when(temp).performRegistration(registrationDTO);
        EmailDTO newUser = temp.register(registrationDTO);
        assertEquals(registrationDTO.getEmail(), newUser.getEmail());
    }

    @Test
    void testDeleteUserIfNotConfirmed() {

        UUID id = new UUID(9583894, 34757);
        ConfirmToken token = ConfirmToken.builder().id(id).build();
        User user = User.builder().confirmToken(token).build();
        UserSecurity userSecurity = UserSecurity.builder().id(id).email("javier_milei@gmail.com").username("Javier Milei").user(user).build();

        doNothing().when(verifyEmailRepository).deleteById(id);
        assertDoesNotThrow(() -> registrationService.deleteUserIfNotConfirmed(userSecurity));
    }

    @Test
    void testPerformRegistration() throws MessagingException {
        RegistrationServiceImpl temp = Mockito.spy(registrationService);

        UserSecurity userSecurity = UserSecurity.builder().username(registrationDTO.getUsername()).email(registrationDTO.getEmail())
                .password(registrationDTO.getPassword()).build();

        when(mapper.toEntityRegistration(registrationDTO)).thenReturn(userSecurity);

        Instant now = Instant.now();
        Instant later = now.minusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();

        User user = User.builder().confirmToken(token).build();

        doReturn(user).when(temp).createNewRegisteredUser(userSecurity);

        when(mapper.toEmail(userSecurity)).thenReturn(new EmailDTO(userSecurity.getEmail()));

        EmailDTO emailDTO = temp.performRegistration(registrationDTO);

        assertEquals(userSecurity.getEmail(), emailDTO.getEmail());
    }

    @Test
    void testSuccessEnableUserIfValid() {

        TokenConfirmationDTO dto = TokenConfirmationDTO.builder().token("SAD88").email("javier_milei@gmail.com").build();

        UserSecurity userSecurity = UserSecurity.builder().enabled(false).build();

        when(tokenService.verifyToken(dto.getEmail(), dto.getToken())).thenReturn(true);

        when(userSecurityRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(userSecurity));

        assertTrue(registrationService.enableIfValid(dto));
    }

    @Test
    void testEnableUserIfValidWrongToken() {

        TokenConfirmationDTO dto = TokenConfirmationDTO.builder().token("SAD88").email("javier_milei@gmail.com").build();

        UserSecurity userSecurity = UserSecurity.builder().enabled(false).build();

        when(tokenService.verifyToken(dto.getEmail(), dto.getToken())).thenReturn(false);

        when(userSecurityRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(userSecurity));

        assertFalse(registrationService.enableIfValid(dto));
    }

    @Test
    void testEnableUserIfValidNoUserFound() {

        TokenConfirmationDTO dto = TokenConfirmationDTO.builder().token("SAD88").email("javier_milei@gmail.com").build();

        when(userSecurityRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertFalse(registrationService.enableIfValid(dto));
    }

    @Test
    void testEnableUserIfValidUserAlreadyEnabled() {

        TokenConfirmationDTO dto = TokenConfirmationDTO.builder().token("SAD88").email("javier_milei@gmail.com").build();

        UserSecurity userSecurity = UserSecurity.builder().enabled(true).build();

        when(userSecurityRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(userSecurity));

        assertFalse(registrationService.enableIfValid(dto));
    }
}