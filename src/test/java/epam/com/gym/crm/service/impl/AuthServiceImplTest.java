package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.TemporarilyBlockException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.BruteForceProtectionService;
import epam.com.gym.crm.service.JwtService;
import epam.com.gym.crm.service.TokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserDAO<User> userDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private BruteForceProtectionService bruteForceProtectionService;
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private User validUser;

    private final String USERNAME = "john.smith";
    private final String PASSWORD = "securePass123";
    private final String TOKEN = "mock.jwt.token";

    @BeforeEach
    void setUp() {
        lenient().when(validUser.getUsername()).thenReturn(USERNAME);
        lenient().when(validUser.getPassword()).thenReturn("encodedPassword");
    }

    @Test
    void authenticate_shouldSucceed_whenCredentialsAreValid() {
        Credentials creds = new Credentials(USERNAME, PASSWORD);

        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(false);
        when(userDAO.findByUsername(USERNAME)).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches(PASSWORD, "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(validUser)).thenReturn(TOKEN);

        String result = authService.authenticate(creds);

        assertEquals(TOKEN, result);
        verify(bruteForceProtectionService).loginSucceeded(USERNAME);
        verify(jwtService).generateToken(validUser);
    }

    @Test
    void authenticate_shouldThrowException_whenUserIsBlocked() {
        Credentials creds = new Credentials(USERNAME, PASSWORD);
        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(true);

        assertThrows(TemporarilyBlockException.class, () -> authService.authenticate(creds));

        verifyNoInteractions(userDAO);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void authenticate_shouldRegisterFailure_whenUserNotFound() {
        Credentials creds = new Credentials("unknown", PASSWORD);
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authService.authenticate(creds));

        verify(bruteForceProtectionService).loginFailed("unknown");
    }

    @Test
    void authenticate_shouldRegisterFailure_whenPasswordIncorrect() {
        Credentials creds = new Credentials(USERNAME, "wrongPass");

        when(userDAO.findByUsername(USERNAME)).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("wrongPass", "encodedPassword")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authService.authenticate(creds));

        verify(bruteForceProtectionService).loginFailed(USERNAME);
        verify(bruteForceProtectionService, never()).loginSucceeded(anyString());
    }

    @Test
    void logout_shouldBlacklistToken() {
        when(jwtService.extractUsername(TOKEN)).thenReturn(USERNAME);

        authService.logout(TOKEN);

        verify(jwtService).extractUsername(TOKEN);
        verify(tokenBlacklistService).blacklist(TOKEN);
    }

}
