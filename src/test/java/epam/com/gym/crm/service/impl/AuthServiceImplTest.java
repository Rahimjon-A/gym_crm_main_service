package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.User;
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
    private UserDAO userDAO;

    @InjectMocks
    private AuthServiceImpl authService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername("john.smith");
        validUser.setPassword("securePass123");
        validUser.setIsActive(true);
    }

    @Test
    void authenticate_shouldSucceed_whenCredentialsAreValid() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertDoesNotThrow(() -> authService.authenticate("john.smith", "securePass123"));
        verify(userDAO, times(1)).findByUsername("john.smith");
    }

    @Test
    void authenticate_shouldThrowException_whenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, 
                () -> authService.authenticate("", "password"));
        verifyNoInteractions(userDAO);
    }

    @Test
    void authenticate_shouldThrowException_whenUserNotFound() {
        when(userDAO.findByUsername("unknown.user")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
                () -> authService.authenticate("unknown.user", "password"));
    }

    @Test
    void authenticate_shouldThrowException_whenUserIsDeactivated() {
        validUser.setIsActive(false);
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertThrows(IllegalStateException.class, 
                () -> authService.authenticate("john.smith", "securePass123"));
    }

    @Test
    void authenticate_shouldThrowException_whenPasswordIsIncorrect() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertThrows(IllegalArgumentException.class, 
                () -> authService.authenticate("john.smith", "wrongPassword"));
    }

    @Test
    void changePassword_shouldUpdatePassword_whenInputsAreValid() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        authService.changePassword("john.smith", "securePass123", "newValidPass456");

        assertEquals("newValidPass456", validUser.getPassword());
        verify(userDAO, times(1)).update(validUser);
    }

    @Test
    void changePassword_shouldThrowException_whenNewPasswordIsTooShort() {
        assertThrows(IllegalArgumentException.class, 
                () -> authService.changePassword("john.smith", "securePass123", "short"));
        verifyNoInteractions(userDAO);
    }

    @Test
    void changePassword_shouldThrowException_whenUserNotFound() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, 
                () -> authService.changePassword("john.smith", "securePass123", "newValidPass456"));
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordIsIncorrect() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertThrows(IllegalArgumentException.class, 
                () -> authService.changePassword("john.smith", "wrongOldPass", "newValidPass456"));
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void activateUser_shouldSetIsActiveToTrue_whenUserIsDeactivated() {
        validUser.setIsActive(false);
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        authService.activateUser("john.smith");

        assertTrue(validUser.getIsActive());
        verify(userDAO, times(1)).update(validUser);
    }

    @Test
    void activateUser_shouldThrowException_whenUserIsAlreadyActive() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser)); // Already active

        assertThrows(IllegalStateException.class, 
                () -> authService.activateUser("john.smith"));
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void activateUser_shouldThrowException_whenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
                () -> authService.activateUser("unknown"));
    }

    @Test
    void deactivateUser_shouldSetIsActiveToFalse_whenUserIsActive() {
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        authService.deactivateUser("john.smith");

        assertFalse(validUser.getIsActive());
        verify(userDAO, times(1)).update(validUser);
    }

    @Test
    void deactivateUser_shouldThrowException_whenUserIsAlreadyDeactivated() {
        validUser.setIsActive(false);
        when(userDAO.findByUsername("john.smith")).thenReturn(Optional.of(validUser));

        assertThrows(IllegalStateException.class, 
                () -> authService.deactivateUser("john.smith"));
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void deactivateUser_shouldThrowException_whenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
                () -> authService.deactivateUser("unknown"));
    }
}
