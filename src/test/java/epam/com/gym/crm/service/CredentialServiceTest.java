package epam.com.gym.crm.service;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.security.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialServiceTest {

    private CredentialService credentialService;
    private PasswordGenerator passwordGenerator;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialService();

        passwordGenerator = mock(PasswordGenerator.class);
        userDAO = mock(UserDAO.class);

        credentialService.setPasswordGenerator(passwordGenerator);
        credentialService.setUserDAO(userDAO);
    }

    @Test
    void generatePassword_shouldDelegateToPasswordGenerator() {
        when(passwordGenerator.generate()).thenReturn("abc1234567");

        String password = credentialService.generatePassword();

        assertEquals("abc1234567", password);
        verify(passwordGenerator, times(1)).generate();
    }

    @Test
    void generateUsername_shouldReturnBaseUsername_whenNotTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe", username);
        verify(userDAO, times(1)).findByUsername("john.doe");
    }

    @Test
    void generateUsername_shouldAppendCounter_whenUsernameTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(new User()));
        when(userDAO.findByUsername("john.doe1")).thenReturn(Optional.empty());

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe1", username);
        verify(userDAO, times(1)).findByUsername("john.doe");
        verify(userDAO, times(1)).findByUsername("john.doe1");
    }

    @Test
    void generateUsername_shouldIncrementCounterMultipleTimes() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(new User()));
        when(userDAO.findByUsername("john.doe1")).thenReturn(Optional.of(new User()));
        when(userDAO.findByUsername("john.doe2")).thenReturn(Optional.empty());

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe2", username);
    }

    @Test
    void generateUsername_shouldThrowException_whenFirstNameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> credentialService.generateUsername(null, "Doe"));
    }

    @Test
    void generateUsername_shouldThrowException_whenLastNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> credentialService.generateUsername("John", " "));
    }

    @Test
    void generateUsername_shouldTrimAndLowercaseInput() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        String username = credentialService.generateUsername("  JOHN  ", "  DOE ");

        assertEquals("john.doe", username);
    }

    @Test
    void isUsernameTaken_shouldReturnTrue_ifTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(new User()));

        assertTrue(credentialService.isUsernameTaken("john.doe"));
    }

    @Test
    void isUsernameTaken_shouldReturnFalse_ifNotTaken() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        assertFalse(credentialService.isUsernameTaken("john.doe"));
    }

    @Test
    void isUsernameTaken_shouldReturnFalse_ifUsernameIsNull() {
        assertFalse(credentialService.isUsernameTaken(null));
        verifyNoInteractions(userDAO);
    }
}
