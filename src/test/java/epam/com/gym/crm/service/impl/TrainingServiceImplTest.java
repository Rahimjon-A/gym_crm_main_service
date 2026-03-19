package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    private static final String BLANK_STRING = "   ";
    private static final String UNKNOWN_USER = "unknown.user";
    private static final String WRONG_USER = "wrong.user";
    private static final Long UNKNOWN_ID = 99L;

    @Mock
    private TrainingDAO trainingDao;
    @Mock
    private UserDAO<Trainee> traineeDao;
    @Mock
    private TrainerDAO trainerDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingCreateRequest validDto;
    private Trainee validTrainee;
    private Trainer validTrainer;
    private TrainingType validTrainingType;
    private Training validTraining;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();

        validDto = new TrainingCreateRequest();
        validDto.setTraineeUsername("john.doe");
        validDto.setTrainerUsername("jane.smith");
        validDto.setTrainingName("Morning Cardio");
        validDto.setTrainingDate(now);
        validDto.setTrainingDuration(60.0);

        validTrainee = new Trainee();
        validTrainee.setId(1L);
        validTrainee.setUsername("john.doe");

        validTrainingType = new TrainingType();
        validTrainingType.setId(3L);
        validTrainingType.setTrainingTypeName("CARDIO");

        validTrainer = new Trainer();
        validTrainer.setId(2L);
        validTrainer.setUsername("jane.smith");
        validTrainer.setSpecialization(validTrainingType);

        validTraining = new Training();
        validTraining.setId(10L);
        validTraining.setTrainee(validTrainee);
        validTraining.setTrainer(validTrainer);
        validTraining.setTrainingType(validTrainingType);
    }

    @Test
    void create_shouldSaveTraining_whenDtoIsValid() {
        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findByUsername(validTrainer.getUsername())).thenReturn(Optional.of(validTrainer));
        when(trainingDao.create(any(Training.class))).thenAnswer(i -> i.getArgument(0));

        Training result = trainingService.create(validDto);

        assertNotNull(result);
        assertEquals(validDto.getTrainingName(), result.getTrainingName());
        assertEquals(validTrainee, result.getTrainee());
        assertEquals(validTrainer, result.getTrainer());
        assertEquals(validTrainingType, result.getTrainingType());
        assertEquals(now, result.getTrainingDate());
        assertEquals(validDto.getTrainingDuration(), result.getTrainingDuration());

        verify(trainingDao, times(1)).create(any(Training.class));
    }

    @Test
    void create_shouldThrowException_whenTraineeNotFound() {
        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(validDto));

        verify(trainerDao, never()).findByUsername(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowException_whenTrainerNotFound() {
        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findByUsername(validTrainer.getUsername())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(validDto));

        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowException_whenDtoIsNull() {
        assertThrows(ValidationException.class, () -> trainingService.create(null));
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldReturnList_whenFilterIsValid() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setUsername(validTrainee.getUsername());

        List<Training> expectedList = List.of(validTraining);
        when(trainingDao.findTraineeTrainingsByCriteria(filter)).thenReturn(expectedList);

        List<Training> result = trainingService.getTraineeTrainingsByCriteria(filter);

        assertEquals(expectedList, result);
        verify(trainingDao, times(1)).findTraineeTrainingsByCriteria(filter);
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldThrowException_whenUsernameIsMissing() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setUsername(BLANK_STRING);

        assertThrows(ValidationException.class, () -> trainingService.getTraineeTrainingsByCriteria(filter));
        verifyNoInteractions(trainingDao);
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldReturnList_whenFilterIsValid() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        filter.setUsername(validTrainer.getUsername());

        List<Training> expectedList = List.of(validTraining);
        when(trainingDao.findTrainerTrainingsByCriteria(filter)).thenReturn(expectedList);

        List<Training> result = trainingService.getTrainerTrainingsByCriteria(filter);

        assertEquals(expectedList, result);
        verify(trainingDao, times(1)).findTrainerTrainingsByCriteria(filter);
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldThrowException_whenUsernameIsMissing() {
        assertThrows(ValidationException.class, () -> trainingService.getTrainerTrainingsByCriteria(null));
        verifyNoInteractions(trainingDao);
    }

    @Test
    void updateTraineeTrainings_shouldUpdateSuccessfully_whenInputsAreValid() {
        List<TrainerAssignmentRequest> assignments = List.of(
                new TrainerAssignmentRequest(validTraining.getId(), validTrainer.getUsername())
        );

        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(validTraining.getId())).thenReturn(Optional.of(validTraining));
        when(trainerDao.findByUsername(validTrainer.getUsername())).thenReturn(Optional.of(validTrainer));

        when(trainingDao.update(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Training> result = trainingService.updateTraineeTrainings(validTrainee.getUsername(), assignments);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(validTrainer, result.get(0).getTrainer());

        verify(trainingDao, times(1)).update(validTraining);
    }

    @Test
    void updateTraineeTrainings_shouldThrowException_whenTraineeNotFound() {
        List<TrainerAssignmentRequest> assignments = List.of(
                new TrainerAssignmentRequest(validTraining.getId(), validTrainer.getUsername())
        );

        when(traineeDao.findByUsername(UNKNOWN_USER)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.updateTraineeTrainings(UNKNOWN_USER, assignments));

        verify(trainingDao, never()).update(any());
    }

    @Test
    void updateTraineeTrainings_shouldThrowException_whenTrainingNotFound() {
        List<TrainerAssignmentRequest> assignments = List.of(
                new TrainerAssignmentRequest(UNKNOWN_ID, validTrainer.getUsername())
        );

        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.updateTraineeTrainings(validTrainee.getUsername(), assignments));

        verify(trainingDao, never()).update(any());
    }

    @Test
    void updateTraineeTrainings_shouldThrowException_whenTrainerNotFound() {
        List<TrainerAssignmentRequest> assignments = List.of(
                new TrainerAssignmentRequest(validTraining.getId(), UNKNOWN_USER)
        );

        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(validTraining.getId())).thenReturn(Optional.of(validTraining));
        when(trainerDao.findByUsername(UNKNOWN_USER)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.updateTraineeTrainings(validTrainee.getUsername(), assignments));

        verify(trainingDao, never()).update(any());
    }

    @Test
    void updateTraineeTrainings_shouldThrowValidationException_whenTrainingBelongsToDifferentTrainee() {
        Trainee ownerTrainee = new Trainee();
        ownerTrainee.setUsername(WRONG_USER);

        validTraining.setTrainee(ownerTrainee);

        List<TrainerAssignmentRequest> assignments = List.of(
                new TrainerAssignmentRequest(validTraining.getId(), validTrainer.getUsername())
        );

        when(traineeDao.findByUsername(validTrainee.getUsername())).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(validTraining.getId())).thenReturn(Optional.of(validTraining));

        assertThrows(ValidationException.class,
                () -> trainingService.updateTraineeTrainings(validTrainee.getUsername(), assignments));

        verify(trainingDao, never()).update(any());
    }

    @Test
    void findById_shouldReturnTraining() {
        when(trainingDao.findById(validTraining.getId())).thenReturn(Optional.of(validTraining));
        assertEquals(validTraining, trainingService.findById(validTraining.getId()));
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(trainingDao.findById(UNKNOWN_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainingService.findById(UNKNOWN_ID));
    }

    @Test
    void findAll_shouldDelegateToDao() {
        List<Training> expectedList = List.of(validTraining);
        when(trainingDao.findAll()).thenReturn(expectedList);

        assertEquals(expectedList, trainingService.findAll());
        verify(trainingDao, times(1)).findAll();
    }
}
