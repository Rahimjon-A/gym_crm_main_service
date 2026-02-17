package epam.com.gym.crm.service;

import epam.com.gym.crm.repository.UsernameRepository;
import epam.com.gym.crm.security.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialServiceTest {

    private CredentialService credentialService;
    private PasswordGenerator passwordGenerator;
    private UsernameRepository usernameRepository;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialService();

        passwordGenerator = mock(PasswordGenerator.class);
        usernameRepository = mock(UsernameRepository.class);

        credentialService.setPasswordGenerator(passwordGenerator);
        credentialService.setUsernameRepository(usernameRepository);
    }

    @Test
    void generatePassword_shouldDelegateToPasswordGenerator() {
        when(passwordGenerator.generate()).thenReturn("abc123");

        String result = credentialService.generatePassword();

        assertEquals("abc123", result);
        verify(passwordGenerator, times(1)).generate();
    }

    @Test
    void generateUsername_shouldReturnBaseUsername_whenNotExists() {
        when(usernameRepository.exists("john.doe")).thenReturn(false);

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe", username);
        verify(usernameRepository, times(1)).exists("john.doe");
    }

    @Test
    void generateUsername_shouldAppendCounter_whenUsernameExists() {
        when(usernameRepository.exists("john.doe")).thenReturn(true);
        when(usernameRepository.exists("john.doe1")).thenReturn(false);

        String username = credentialService.generateUsername("John", "Doe");

        assertEquals("john.doe1", username);
        verify(usernameRepository).exists("john.doe");
        verify(usernameRepository).exists("john.doe1");
    }

    @Test
    void generateUsername_shouldIncrementCounterMultipleTimes() {
        when(usernameRepository.exists("john.doe")).thenReturn(true);
        when(usernameRepository.exists("john.doe1")).thenReturn(true);
        when(usernameRepository.exists("john.doe2")).thenReturn(false);

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
        when(usernameRepository.exists("john.doe")).thenReturn(false);

        String username = credentialService.generateUsername("  JOHN  ", "  DOE ");

        assertEquals("john.doe", username);
    }
}
