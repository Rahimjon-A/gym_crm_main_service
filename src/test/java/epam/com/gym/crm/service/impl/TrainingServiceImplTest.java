package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.TrainerWorkloadService;
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
    private static final String TRAINEE_USERNAME = "john.doe";
    private static final String TRAINER_USERNAME = "jane.smith";
    private static final String TRAINING_NAME = "Morning Cardio";
    private static final int TRAINING_DURATION= 60;
    private static final Long TRAINING_ID = 10L;
    private static final String BEARER_TOKEN = "Bearer test.jwt.token";

    @Mock
    private TrainingDAO trainingDao;
    @Mock
    private UserDAO<Trainee> traineeDao;
    @Mock
    private TrainerDAO trainerDao;
    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training validInputTraining;
    private Trainee validTrainee;
    private Trainer validTrainer;
    private TrainingType validTrainingType;
    private Training validTraining;

    @BeforeEach
    void setUp() {
        Date now = new Date();

        validTrainee = new Trainee();
        validTrainee.setId(1L);
        validTrainee.setUsername(TRAINEE_USERNAME);

        validTrainingType = new TrainingType();
        validTrainingType.setId(3L);
        validTrainingType.setTrainingTypeName("CARDIO");

        validTrainer = new Trainer();
        validTrainer.setId(2L);
        validTrainer.setUsername(TRAINER_USERNAME);
        validTrainer.setSpecialization(validTrainingType);

        Trainee dummyTrainee = new Trainee();
        dummyTrainee.setUsername(TRAINEE_USERNAME);

        Trainer dummyTrainer = new Trainer();
        dummyTrainer.setUsername(TRAINER_USERNAME);

        validInputTraining = new Training();
        validInputTraining.setTrainee(dummyTrainee);
        validInputTraining.setTrainer(dummyTrainer);
        validInputTraining.setTrainingName(TRAINING_NAME);
        validInputTraining.setTrainingDate(now);
        validInputTraining.setTrainingDuration(TRAINING_DURATION);

        validTraining = new Training();
        validTraining.setId(TRAINING_ID);
        validTraining.setTrainee(validTrainee);
        validTraining.setTrainer(validTrainer);
        validTraining.setTrainingType(validTrainingType);
        validTraining.setTrainingDate(now);
        validTraining.setTrainingDuration(TRAINING_DURATION);
    }

    @Test
    void create_shouldSaveTrainingAndNotifyWorkload_whenInputIsValid() {
        when(traineeDao.findByUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findByUsername(TRAINER_USERNAME)).thenReturn(Optional.of(validTrainer));
        when(trainingDao.create(any(Training.class))).thenAnswer(i -> i.getArgument(0));

        Training result = trainingService.create(validInputTraining);

        assertNotNull(result);
        assertEquals(TRAINING_NAME, result.getTrainingName());
        assertEquals(validTrainee, result.getTrainee());
        assertEquals(validTrainer, result.getTrainer());
        assertEquals(validTrainingType, result.getTrainingType());

        verify(trainingDao).create(any(Training.class));
        verify(trainerWorkloadService).notifyAdd(eq(validTrainer), any(Training.class));
    }

    @Test
    void create_shouldStillSaveTraining_whenWorkloadServiceFails() {
        when(traineeDao.findByUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findByUsername(TRAINER_USERNAME)).thenReturn(Optional.of(validTrainer));
        when(trainingDao.create(any(Training.class))).thenAnswer(i -> i.getArgument(0));

        doNothing().when(trainerWorkloadService).notifyAdd(any(), any());

        Training result = trainingService.create(validInputTraining);

        assertNotNull(result);
        verify(trainingDao).create(any(Training.class));
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
        when(traineeDao.findByUsername(TRAINEE_USERNAME)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.create(validInputTraining));

        verify(trainerDao, never()).findByUsername(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
        when(traineeDao.findByUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findByUsername(TRAINER_USERNAME)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.create(validInputTraining));

        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowValidationException_whenTrainingIsNull() {
        assertThrows(ValidationException.class, () -> trainingService.create(null));
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldReturnList_whenFilterIsValid() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setUsername(TRAINEE_USERNAME);

        List<Training> expected = List.of(validTraining);
        when(trainingDao.findTraineeTrainingsByCriteria(filter)).thenReturn(expected);

        assertEquals(expected, trainingService.getTraineeTrainingsByCriteria(filter));
        verify(trainingDao).findTraineeTrainingsByCriteria(filter);
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldThrowValidationException_whenUsernameIsBlank() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setUsername(BLANK_STRING);

        assertThrows(ValidationException.class,
                () -> trainingService.getTraineeTrainingsByCriteria(filter));
        verifyNoInteractions(trainingDao);
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldReturnList_whenFilterIsValid() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        filter.setUsername(TRAINER_USERNAME);

        List<Training> expected = List.of(validTraining);
        when(trainingDao.findTrainerTrainingsByCriteria(filter)).thenReturn(expected);

        assertEquals(expected, trainingService.getTrainerTrainingsByCriteria(filter));
        verify(trainingDao).findTrainerTrainingsByCriteria(filter);
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldThrowValidationException_whenFilterIsNull() {
        assertThrows(ValidationException.class,
                () -> trainingService.getTrainerTrainingsByCriteria(null));
        verifyNoInteractions(trainingDao);
    }

    @Test
    void updateTraineeTrainings_shouldUpdateSuccessfully_whenInputsAreValid() {
        Trainer dummyTrainer = new Trainer();
        dummyTrainer.setUsername(TRAINER_USERNAME);

        Training assignment = new Training();
        assignment.setId(TRAINING_ID);
        assignment.setTrainer(dummyTrainer);

        when(traineeDao.findByUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(validTraining));
        when(trainerDao.findByUsername(TRAINER_USERNAME)).thenReturn(Optional.of(validTrainer));
        when(trainingDao.update(any())).thenAnswer(i -> i.getArgument(0));

        List<Training> result = trainingService.updateTraineeTrainings(TRAINEE_USERNAME, List.of(assignment));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(validTrainer, result.get(0).getTrainer());
        verify(trainingDao).update(validTraining);
    }

    @Test
    void updateTraineeTrainings_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
        Trainer dummyTrainer = new Trainer();
        dummyTrainer.setUsername(TRAINER_USERNAME);

        Training assignment = new Training();
        assignment.setId(TRAINING_ID);
        assignment.setTrainer(dummyTrainer);

        when(traineeDao.findByUsername(UNKNOWN_USER)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.updateTraineeTrainings(UNKNOWN_USER, List.of(assignment)));
        verify(trainingDao, never()).update(any());
    }

    @Test
    void updateTraineeTrainings_shouldThrowValidationException_whenTrainingBelongsToDifferentTrainee() {
        Trainee otherTrainee = new Trainee();
        otherTrainee.setUsername(WRONG_USER);
        validTraining.setTrainee(otherTrainee);

        Trainer dummyTrainer = new Trainer();
        dummyTrainer.setUsername(TRAINER_USERNAME);

        Training assignment = new Training();
        assignment.setId(TRAINING_ID);
        assignment.setTrainer(dummyTrainer);

        when(traineeDao.findByUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(validTrainee));
        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(validTraining));

        assertThrows(ValidationException.class,
                () -> trainingService.updateTraineeTrainings(TRAINEE_USERNAME, List.of(assignment)));
        verify(trainingDao, never()).update(any());
    }

    @Test
    void findById_shouldReturnTraining_whenExists() {
        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(validTraining));
        assertEquals(validTraining, trainingService.findById(TRAINING_ID));
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenNotFound() {
        when(trainingDao.findById(UNKNOWN_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainingService.findById(UNKNOWN_ID));
    }

    @Test
    void findAll_shouldDelegateToDao() {
        List<Training> expected = List.of(validTraining);
        when(trainingDao.findAll()).thenReturn(expected);

        assertEquals(expected, trainingService.findAll());
        verify(trainingDao).findAll();
    }
}
