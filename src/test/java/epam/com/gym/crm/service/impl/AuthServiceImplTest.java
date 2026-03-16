package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.model.common.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserDAO<User> userDAO;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private User validUser;

    @BeforeEach
    void setUp() {
        lenient().when(validUser.getUsername()).thenReturn("john.smith");
        lenient().when(validUser.getPassword()).thenReturn("securePass123");
        lenient().when(validUser.isActive()).thenReturn(true);
    }

    @Test
    void authenticate_shouldSucceed_whenCredentialsAreValid() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertDoesNotThrow(() -> authService.authenticate( new Credentials("john.smith", "securePass123")));
        verify(userDAO, times(1)).findByUsername("john.smith");
    }

    @Test
    void authenticate_shouldThrowException_whenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, 
                () -> authService.authenticate(new Credentials("", "password")));
        verifyNoInteractions(userDAO);
    }

    @Test
    void authenticate_shouldThrowException_whenUserNotFound() {
        when(userDAO.findByUsername("unknown.user")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
                () -> authService.authenticate(new Credentials("unknown.user", "password")));
    }

    @Test
    void authenticate_shouldThrowException_whenPasswordIsIncorrect() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertThrows(IllegalArgumentException.class, 
                () -> authService.authenticate(new Credentials("john.smith", "wrongPassword")));
    }

    @Test
    void authenticate_shouldThrowException_whenUserIsInactive() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));
        when(validUser.isActive()).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.authenticate(new Credentials("john.smith", "securePass123")));
    }
}
