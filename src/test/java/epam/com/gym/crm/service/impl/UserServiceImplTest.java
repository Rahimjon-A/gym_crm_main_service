package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.utility.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    private static class TestUser extends User {}

    @BeforeEach
    void setUp() {
        testUser = new TestUser();
        testUser.setUsername("john.doe");
        testUser.setPassword("securePass123");
        testUser.setActive(true);
    }

    @Test
    void generatePassword_shouldDelegateToPasswordGenerator() {
        try (MockedStatic<PasswordGenerator> mockedStatic = Mockito.mockStatic(PasswordGenerator.class)) {
            mockedStatic.when(PasswordGenerator::generate).thenReturn("abc1234567");

            String password = userService.generatePassword();

            assertEquals("abc1234567", password);
            mockedStatic.verify(PasswordGenerator::generate, times(1));
        }
    }

    @Test
    void generateUsername_shouldReturnBaseUsername_whenNotTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        String username = userService.generateUsername("John", "Doe");

        assertEquals("john.doe", username);
        verify(userDAO, times(1)).findByUsername("john.doe");
    }

    @Test
    void generateUsername_shouldAppendCounter_whenUsernameTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(userDAO.findByUsername("john.doe1")).thenReturn(Optional.empty());

        String username = userService.generateUsername("John", "Doe");

        assertEquals("john.doe1", username);
    }

    @Test
    void generateUsername_shouldIncrementCounterMultipleTimes() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(userDAO.findByUsername("john.doe1")).thenReturn(Optional.of(testUser));
        when(userDAO.findByUsername("john.doe2")).thenReturn(Optional.empty());

        String username = userService.generateUsername("John", "Doe");

        assertEquals("john.doe2", username);
    }

    @Test
    void generateUsername_shouldThrowException_whenFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.generateUsername(null, "Doe"));
    }

    @Test
    void generateUsername_shouldThrowException_whenLastNameBlank() {
        assertThrows(IllegalArgumentException.class, () -> userService.generateUsername("John", " "));
    }

    @Test
    void generateUsername_shouldTrimAndLowercaseInput() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        String username = userService.generateUsername("  JOHN  ", "  DOE ");

        assertEquals("john.doe", username);
    }

    @Test
    void isUsernameTaken_shouldReturnTrue_ifTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        assertTrue(userService.isUsernameTaken("john.doe"));
    }

    @Test
    void isUsernameTaken_shouldReturnFalse_ifNotTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());
        assertFalse(userService.isUsernameTaken("john.doe"));
    }

    @Test
    void isUsernameTaken_shouldReturnFalse_ifUsernameIsNull() {
        assertFalse(userService.isUsernameTaken(null));
        verifyNoInteractions(userDAO);
    }

    @Test
    void changePassword_shouldUpdatePassword_whenValid() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        userService.changePassword("john.doe", "securePass123", "newPassword123");

        assertEquals("newPassword123", testUser.getPassword());
        verify(userDAO, times(1)).update(testUser);
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordIncorrect() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, 
                () -> userService.changePassword("john.doe", "wrongPass", "newPassword123"));
        verify(userDAO, never()).update(any());
    }

    @Test
    void changePassword_shouldThrowException_whenNewPasswordTooShort() {
        assertThrows(IllegalArgumentException.class, 
                () -> userService.changePassword("john.doe", "oldPass123", "short"));
        verifyNoInteractions(userDAO);
    }

    @Test
    void changePassword_shouldThrowException_whenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, 
                () -> userService.changePassword("unknown", "oldPass123", "newPassword123"));
    }

    @Test
    void activateUser_shouldSetActiveTrue_whenUserIsInactive() {
        testUser.setActive(false);
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        userService.activateUser("john.doe");

        assertTrue(testUser.isActive());
        verify(userDAO, times(1)).update(testUser);
    }

    @Test
    void activateUser_shouldThrowException_whenUserAlreadyActive() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        assertThrows(IllegalStateException.class, () -> userService.activateUser("john.doe"));
        verify(userDAO, never()).update(any());
    }

    @Test
    void deactivateUser_shouldSetActiveFalse_whenUserIsActive() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        userService.deactivateUser("john.doe");

        assertFalse(testUser.isActive());
        verify(userDAO, times(1)).update(testUser);
    }

    @Test
    void deactivateUser_shouldThrowException_whenUserAlreadyInactive() {
        testUser.setActive(false);
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        assertThrows(IllegalStateException.class, () -> userService.deactivateUser("john.doe"));
        verify(userDAO, never()).update(any());
    }
}