package example.com.springcoretask1.util;

import example.com.springcoretask1.dao.TraineeDAO;
import example.com.springcoretask1.dao.TrainerDAO;
import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialsUtilTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private CredentialsUtil credentialsUtil;

    private Trainer trainer;
    private Trainee trainee;

    @BeforeEach
    void setup() {
        trainer = new Trainer();
        trainee = new Trainee();
    }

    @Test
    void generateUsername_shouldReturnBaseWhenNoDuplicates() {
        when(trainerDAO.findAll()).thenReturn(List.of(trainer));
        when(traineeDAO.findAll()).thenReturn(List.of(trainee));

        String username = credentialsUtil.generateUsername("John", "Smith");

        assertEquals("john.smith", username);
    }

    @Test
    void generateUsername_shouldAppend1WhenDuplicateExists() {
        trainer.setUsername("john.smith");

        when(trainerDAO.findAll()).thenReturn(List.of(trainer));
        when(traineeDAO.findAll()).thenReturn(List.of());

        String username = credentialsUtil.generateUsername("John", "Smith");

        assertEquals("john.smith1", username);
    }

    @Test
    void generateUsername_shouldAppendNextAvailableNumber() {
        trainer.setUsername("john.smith");
        Trainer trainer2 = new Trainer();
        trainer2.setUsername("john.smith1");

        when(trainerDAO.findAll()).thenReturn(List.of(trainer, trainer2));
        when(traineeDAO.findAll()).thenReturn(List.of());

        String username = credentialsUtil.generateUsername("John", "Smith");

        assertEquals("john.smith2", username);
    }

    @Test
    void generateUsername_shouldThrowExceptionWhenFirstNameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> credentialsUtil.generateUsername(null, "Smith"));
    }

    @Test
    void generateUsername_shouldThrowExceptionWhenLastNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> credentialsUtil.generateUsername("John", " "));
    }

    @Test
    void generateUsername_shouldBeCaseInsensitive() {
        trainer.setUsername("john.smith");
        when(trainerDAO.findAll()).thenReturn(List.of(trainer));
        when(traineeDAO.findAll()).thenReturn(List.of());

        String username = credentialsUtil.generateUsername("JOHN", "SMITH");

        assertEquals("john.smith1", username);
    }

    @Test
    void generatePassword_shouldHaveLength10() {
        String password = credentialsUtil.generatePassword();
        assertEquals(10, password.length());
    }

    @Test
    void generatePassword_shouldGenerateDifferentPasswords() {
        String p1 = credentialsUtil.generatePassword();
        String p2 = credentialsUtil.generatePassword();

        assertNotEquals(p1, p2);
    }

}
