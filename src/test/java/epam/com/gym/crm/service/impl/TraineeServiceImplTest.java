package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDAO traineeDao;
    @Mock
    private TrainingDAO trainingDao;
    @Mock
    private TrainerDAO trainerDao;
    @Mock
    private CredentialService credentialService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private TraineeDTO validDto;
    private Trainee validTrainee;
    private User validUser;
    private Date pastDate;

    @BeforeEach
    void setUp() {
        pastDate = new Date(System.currentTimeMillis() - 10000000L);

        validDto = new TraineeDTO();
        validDto.setFirstName("John");
        validDto.setLastName("Doe");
        validDto.setDateOfBirth(pastDate);
        validDto.setAddress("123 Main St");

        validUser = new User();
        validUser.setUsername("john.doe");
        validUser.setPassword("pass123");
        validUser.setIsActive(true);
        validUser.setFirstName("John");
        validUser.setLastName("Doe");

        validTrainee = new Trainee();
        validTrainee.setId(1L);
        validTrainee.setUser(validUser);
        validTrainee.setDateOfBirth(pastDate);
        validTrainee.setAddress("123 Main St");
    }

    @Test
    void create_shouldSaveTrainee_whenDtoIsValid() {
        when(credentialService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(credentialService.generatePassword()).thenReturn("pass123");
        when(traineeDao.create(any(Trainee.class))).thenAnswer(i -> i.getArgument(0));

        Trainee result = traineeService.create(validDto);

        assertNotNull(result);
        assertEquals("john.doe", result.getUser().getUsername());
        assertEquals("pass123", result.getUser().getPassword());
        assertTrue(result.getUser().getIsActive());
        verify(traineeDao, times(1)).create(any(Trainee.class));
    }

    @Test
    void create_shouldThrowException_whenDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> traineeService.create(null));
        verifyNoInteractions(traineeDao);
    }

    @Test
    void create_shouldThrowException_whenFirstNameIsBlank() {
        validDto.setFirstName("   ");
        assertThrows(IllegalArgumentException.class, () -> traineeService.create(validDto));
    }

    @Test
    void create_shouldThrowException_whenDateOfBirthIsInFuture() {
        validDto.setDateOfBirth(new Date(System.currentTimeMillis() + 10000000L)); // Future date
        assertThrows(IllegalArgumentException.class, () -> traineeService.create(validDto));
    }

    @Test
    void update_shouldUpdateFields_whenValid() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(validTrainee));
        when(traineeDao.update(any(Trainee.class))).thenAnswer(i -> i.getArgument(0));

        TraineeDTO updateDto = new TraineeDTO();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setAddress("456 New Ave");

        Trainee result = traineeService.update(1L, updateDto);

        assertEquals("Jane", result.getUser().getFirstName());
        assertEquals("Smith", result.getUser().getLastName());
        assertEquals("456 New Ave", result.getAddress());
        verify(traineeDao, times(1)).update(validTrainee);
    }

    @Test
    void update_shouldThrowException_whenTraineeNotFound() {
        when(traineeDao.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> traineeService.update(99L, validDto));
    }

    @Test
    void updateTraineeTrainings_shouldUpdateTrainings_whenValid() {
        Long traineeId = 1L;
        Long trainingId = 10L;
        Long trainerId = 100L;

        Training training = new Training();
        training.setId(trainingId);
        training.setTrainee(validTrainee);

        User trainerUser = new User();
        trainerUser.setIsActive(true);
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        trainer.setUser(trainerUser);

        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        Map<Long, Long> map = Map.of(trainingId, trainerId);

        traineeService.updateTraineeTrainings(traineeId, map);

        assertEquals(trainer, training.getTrainer());
        verify(trainingDao, times(1)).update(training);
    }

    @Test
    void updateTraineeTrainings_shouldThrowException_whenTrainingDoesNotBelongToTrainee() {
        Long traineeId = 1L;
        Long trainingId = 10L;
        Long trainerId = 100L;

        Trainee otherTrainee = new Trainee();
        otherTrainee.setId(2L);

        Training training = new Training();
        training.setId(trainingId);
        training.setTrainee(otherTrainee);

        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        Map<Long, Long> map = Map.of(trainingId, trainerId);

        assertThrows(IllegalArgumentException.class, () -> traineeService.updateTraineeTrainings(traineeId, map));
        verify(trainingDao, never()).update(any());
    }

    @Test
    void updateTraineeTrainings_shouldThrowException_whenTrainerIsInactive() {
        Long traineeId = 1L;
        Long trainingId = 10L;
        Long trainerId = 100L;

        Training training = new Training();
        training.setId(trainingId);
        training.setTrainee(validTrainee);

        User inactiveUser = new User();
        inactiveUser.setIsActive(false);
        Trainer inactiveTrainer = new Trainer();
        inactiveTrainer.setId(trainerId);
        inactiveTrainer.setUser(inactiveUser);

        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(inactiveTrainer));

        Map<Long, Long> map = Map.of(trainingId, trainerId);

        assertThrows(IllegalStateException.class, () -> traineeService.updateTraineeTrainings(traineeId, map));
    }

    @Test
    void findByUsername_shouldReturnTrainee() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(validTrainee));
        assertEquals(validTrainee, traineeService.findByUsername("john.doe"));
    }

    @Test
    void deleteByUsername_shouldFindAndHardDelete() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(validTrainee));

        traineeService.deleteByUsername("john.doe");

        verify(traineeDao, times(1)).findByUsername("john.doe");
        verify(traineeDao, times(1)).delete(1L);
    }

    @Test
    void getUnassignedTrainers_shouldDelegateToDao() {
        List<Trainer> mockTrainers = List.of(new Trainer());
        when(traineeDao.getUnassignedTrainers("john.doe")).thenReturn(mockTrainers);

        assertEquals(mockTrainers, traineeService.getUnassignedTrainers("john.doe"));
        verify(traineeDao, times(1)).getUnassignedTrainers("john.doe");
    }

    @Test
    void findAll_shouldDelegateToDao() {
        List<Trainee> mockList = List.of(validTrainee);
        when(traineeDao.findAll()).thenReturn(mockList);

        assertEquals(mockList, traineeService.findAll());
        verify(traineeDao, times(1)).findAll();
    }
}
