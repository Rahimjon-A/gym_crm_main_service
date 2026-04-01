package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.TrainingType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDao;
    @Mock
    private TrainingTypeDAO trainingTypeDAO;
    @Mock
    private UserServiceImpl credentialService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer validInputTrainer;
    private Trainer validTrainer;
    private TrainingType validTrainingType;

    @BeforeEach
    void setUp() {
        validTrainingType = new TrainingType();
        validTrainingType.setId(2L);
        validTrainingType.setTrainingTypeName("YOGA");

        validInputTrainer = new Trainer();
        validInputTrainer.setFirstName("John");
        validInputTrainer.setLastName("Smith");
        validInputTrainer.setActive(true);

        TrainingType dummyType = new TrainingType();
        dummyType.setId(2L);
        validInputTrainer.setSpecialization(dummyType);

        validTrainer = new Trainer();
        validTrainer.setUsername("john.smith");
        validTrainer.setPassword("abc1234567");
        validTrainer.setActive(true);
        validTrainer.setFirstName("John");
        validTrainer.setLastName("Smith");
        validTrainer.setId(1L);
        validTrainer.setSpecialization(validTrainingType);

        lenient().when(passwordEncoder.encode(anyString())).thenReturn("hashed_dummy_password");
    }

    @Test
    void create_shouldSaveTrainer_whenInputIsValid() {
        when(credentialService.generateUsername("John", "Smith")).thenReturn("john.smith");
        when(credentialService.generatePassword()).thenReturn("abc1234567");
        when(trainingTypeDAO.findById(2L)).thenReturn(Optional.of(validTrainingType));
        when(trainerDao.create(any(Trainer.class))).thenAnswer(i -> i.getArgument(0));

        Trainer result = trainerService.create(validInputTrainer);

        assertNotNull(result);
        assertEquals("john.smith", result.getUsername());
        assertEquals("abc1234567", result.getPassword());
        assertTrue(result.isActive());
        assertEquals(validTrainingType, result.getSpecialization());

        verify(trainingTypeDAO, times(1)).findById(2L);
        verify(trainerDao, times(1)).create(any(Trainer.class));
    }

    @Test
    void create_shouldThrowException_whenInputIsNull() {
        assertThrows(ValidationException.class, () -> trainerService.create(null));
        verifyNoInteractions(trainerDao);
    }

    @Test
    void create_shouldThrowException_whenFirstNameIsBlank() {
        validInputTrainer.setFirstName("   ");
        assertThrows(ValidationException.class, () -> trainerService.create(validInputTrainer));
        verifyNoInteractions(trainerDao);
    }

    @Test
    void create_shouldThrowException_whenSpecializationIsNull() {
        validInputTrainer.setSpecialization(null);
        assertThrows(ValidationException.class, () -> trainerService.create(validInputTrainer));
        verifyNoInteractions(trainerDao);
    }

    @Test
    void create_shouldThrowException_whenSpecializationNotFound() {
        when(credentialService.generateUsername("John", "Smith")).thenReturn("john.smith");
        when(credentialService.generatePassword()).thenReturn("abc1234567");
        when(trainingTypeDAO.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainerService.create(validInputTrainer));
        verify(trainerDao, never()).create(any(Trainer.class));
    }

    @Test
    void update_shouldUpdateFields_whenValidAndChanged() {
        when(trainerDao.findByUsername("john.smith")).thenReturn(Optional.of(validTrainer));

        TrainingType newTrainingType = new TrainingType();
        newTrainingType.setId(3L);
        newTrainingType.setTrainingTypeName("CARDIO");

        when(trainingTypeDAO.findById(3L)).thenReturn(Optional.of(newTrainingType));
        when(trainerDao.update(any(Trainer.class))).thenAnswer(i -> i.getArgument(0));

        Trainer updateInput = new Trainer();
        updateInput.setFirstName("Mike");
        updateInput.setLastName("Jones");
        updateInput.setActive(true);

        TrainingType updateDummyType = new TrainingType();
        updateDummyType.setId(3L);
        updateInput.setSpecialization(updateDummyType);

        Trainer result = trainerService.update("john.smith", updateInput);

        assertEquals("Mike", result.getFirstName());
        assertEquals("Jones", result.getLastName());
        assertEquals(3L, result.getSpecialization().getId());
        verify(trainerDao, times(1)).update(validTrainer);
    }

    @Test
    void update_shouldNotFetchSpecialization_whenIdIsUnchanged() {
        when(trainerDao.findByUsername("john.smith")).thenReturn(Optional.of(validTrainer));
        when(trainerDao.update(any(Trainer.class))).thenAnswer(i -> i.getArgument(0));

        Trainer updateInput = new Trainer();
        updateInput.setFirstName("John");
        updateInput.setLastName("Smith");
        updateInput.setActive(true);

        TrainingType updateDummyType = new TrainingType();
        updateDummyType.setId(2L);
        updateInput.setSpecialization(updateDummyType);

        trainerService.update("john.smith", updateInput);

        verify(trainingTypeDAO, never()).findById(anyLong());
        verify(trainerDao, times(1)).update(validTrainer);
    }

    @Test
    void update_shouldThrowException_whenTrainerNotFound() {
        when(trainerDao.findByUsername("unknown.user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainerService.update("unknown.user", validInputTrainer));
    }

    @Test
    void update_shouldThrowException_whenNewSpecializationNotFound() {
        when(trainerDao.findByUsername("john.smith")).thenReturn(Optional.of(validTrainer));
        when(trainingTypeDAO.findById(99L)).thenReturn(Optional.empty());

        Trainer updateInput = new Trainer();
        updateInput.setFirstName("John");
        updateInput.setLastName("Smith");
        updateInput.setActive(true);

        TrainingType updateDummyType = new TrainingType();
        updateDummyType.setId(99L);
        updateInput.setSpecialization(updateDummyType);

        assertThrows(EntityNotFoundException.class, () -> trainerService.update("john.smith", updateInput));
        verify(trainerDao, never()).update(any(Trainer.class));
    }

    @Test
    void getUnassignedTrainers_shouldDelegateToDao() {
        List<Trainer> mockTrainers = List.of(new Trainer());
        when(trainerDao.getUnassignedTrainers("john.smith")).thenReturn(mockTrainers);

        assertEquals(mockTrainers, trainerDao.getUnassignedTrainers("john.smith"));
        verify(trainerDao, times(1)).getUnassignedTrainers("john.smith");
    }

    @Test
    void getUnassignedTrainers_shouldReturnList_whenUsernameIsValid() {
        List<Trainer> expectedList = List.of(validTrainer);
        when(trainerDao.getUnassignedTrainers("john.smith")).thenReturn(expectedList);

        List<Trainer> result = trainerService.getUnassignedTrainers("john.smith");

        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(trainerDao, times(1)).getUnassignedTrainers("john.smith");
    }

    @Test
    void getUnassignedTrainers_shouldThrowException_whenUsernameIsNull() {
        assertThrows(ValidationException.class,
                () -> trainerService.getUnassignedTrainers(null));

        verify(trainerDao, never()).getUnassignedTrainers(any());
    }

    @Test
    void getUnassignedTrainers_shouldThrowException_whenUsernameIsBlank() {
        assertThrows(ValidationException.class,
                () -> trainerService.getUnassignedTrainers(" "));

        verify(trainerDao, never()).getUnassignedTrainers(any());
    }

    @Test
    void findById_shouldReturnTrainer() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(validTrainer));
        assertEquals(validTrainer, trainerService.findById(1L));
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainerService.findById(1L));
    }

    @Test
    void findByUsername_shouldReturnTrainer() {
        when(trainerDao.findByUsername("john.smith")).thenReturn(Optional.of(validTrainer));
        assertEquals(validTrainer, trainerService.findByUsername("john.smith"));
    }

    @Test
    void findByUsername_shouldThrowException_whenNotFound() {
        when(trainerDao.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainerService.findByUsername("unknown"));
    }

    @Test
    void findAll_shouldDelegateToDao() {
        List<Trainer> mockList = List.of(validTrainer);
        when(trainerDao.findAll()).thenReturn(mockList);

        assertEquals(mockList, trainerService.findAll());
        verify(trainerDao, times(1)).findAll();
    }
}
