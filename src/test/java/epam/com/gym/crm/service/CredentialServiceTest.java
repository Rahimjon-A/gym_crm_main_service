package epam.com.gym.crm.service;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.security.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialServiceTest {

    private CredentialService credentialService;
    private PasswordGenerator passwordGenerator;
    private UserDAO<Trainer> trainerDAO;
    private UserDAO<Trainee> traineeDAO;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialService();

        passwordGenerator = mock(PasswordGenerator.class);
        trainerDAO = mock(UserDAO.class);
        traineeDAO = mock(UserDAO.class);

        credentialService.setPasswordGenerator(passwordGenerator);
        credentialService.setTrainerDAO(trainerDAO);
        credentialService.setTraineeDAO(traineeDAO);
    }

    @Test
    void generatePassword_shouldDelegateToPasswordGenerator() {
        when(passwordGenerator.generate()).thenReturn("abc123");

        String password = credentialService.generatePassword();

        assertEquals("abc123", password);
        verify(passwordGenerator, times(1)).generate();
    }

    @Test
    void generateUsername_shouldReturnBaseUsername_whenNotTaken() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe", username);
        verify(trainerDAO).existsByUsername("john.doe");
        verify(traineeDAO).existsByUsername("john.doe");
    }

    @Test
    void generateUsername_shouldAppendCounter_whenUsernameTaken() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(true);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);
        when(trainerDAO.existsByUsername("john.doe1")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe1")).thenReturn(false);

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe1", username);
    }

    @Test
    void generateUsername_shouldIncrementCounterMultipleTimes() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(true);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        when(trainerDAO.existsByUsername("john.doe1")).thenReturn(true);
        when(traineeDAO.existsByUsername("john.doe1")).thenReturn(false);

        when(trainerDAO.existsByUsername("john.doe2")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe2")).thenReturn(false);

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
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        String username = credentialService.generateUsername("  JOHN  ", "  DOE ");

        assertEquals("john.doe", username);
    }

    @Test
    void isUsernameTaken_shouldReturnTrue_ifTaken() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(true);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        assertTrue(credentialService.isUsernameTaken("john.doe"));
    }

    @Test
    void isUsernameTaken_shouldReturnFalse_ifNotTaken() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        assertFalse(credentialService.isUsernameTaken("john.doe"));
    }
}
