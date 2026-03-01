package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.dto.TraineeTrainingFilter;
import epam.com.gym.crm.dto.TrainerTrainingFilter;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
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

    @Mock
    private TrainingDAO trainingDao;
    @Mock
    private TraineeDAO traineeDao;
    @Mock
    private TrainerDAO trainerDao;
    @Mock
    private TrainingTypeDAO trainingTypeDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO validDto;
    private Trainee validTrainee;
    private Trainer validTrainer;
    private TrainingType validTrainingType;
    private Training validTraining;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();

        validDto = new TrainingDTO();
        validDto.setTraineeId(1L);
        validDto.setTrainerId(2L);
        validDto.setTrainingTypeId(3L);
        validDto.setTrainingName("Morning Cardio");
        validDto.setTrainingDate(now);
        validDto.setTrainingDuration(60.0);

        validTrainee = new Trainee();
        validTrainee.setId(1L);

        validTrainer = new Trainer();
        validTrainer.setId(2L);

        validTrainingType = new TrainingType();
        validTrainingType.setId(3L);
        validTrainingType.setTrainingTypeName("CARDIO");

        validTraining = new Training();
        validTraining.setId(10L);
        validTraining.setTrainee(validTrainee);
        validTraining.setTrainer(validTrainer);
        validTraining.setTrainingType(validTrainingType);
    }

    @Test
    void create_shouldSaveTraining_whenDtoIsValid() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findById(2L)).thenReturn(Optional.of(validTrainer));
        when(trainingTypeDao.findById(3L)).thenReturn(Optional.of(validTrainingType));
        when(trainingDao.create(any(Training.class))).thenAnswer(i -> i.getArgument(0));

        Training result = trainingService.create(validDto);

        assertNotNull(result);
        assertEquals("Morning Cardio", result.getTrainingName());
        assertEquals(validTrainee, result.getTrainee());
        assertEquals(validTrainer, result.getTrainer());
        assertEquals(validTrainingType, result.getTrainingType());
        assertEquals(now, result.getTrainingDate());
        assertEquals(60.0, result.getTrainingDuration());

        verify(trainingDao, times(1)).create(any(Training.class));
    }

    @Test
    void create_shouldThrowException_whenTraineeNotFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(validDto));

        verify(trainerDao, never()).findById(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowException_whenTrainerNotFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(validDto));

        verify(trainingTypeDao, never()).findById(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowException_whenTrainingTypeNotFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(validTrainee));
        when(trainerDao.findById(2L)).thenReturn(Optional.of(validTrainer));
        when(trainingTypeDao.findById(3L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(validDto));

        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowException_whenDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> trainingService.create(null));
    }

    @Test
    void create_shouldThrowException_whenMissingMandatoryFields() {
        validDto.setTraineeId(null);
        assertThrows(IllegalArgumentException.class, () -> trainingService.create(validDto));
        validDto.setTraineeId(1L);

        validDto.setTrainingName("   ");
        assertThrows(IllegalArgumentException.class, () -> trainingService.create(validDto));
        validDto.setTrainingName("Valid Name");

        validDto.setTrainingDuration(0.0);
        assertThrows(IllegalArgumentException.class, () -> trainingService.create(validDto));

        verifyNoInteractions(trainingDao);
    }


    @Test
    void getTraineeTrainingsByCriteria_shouldReturnList_whenFilterIsValid() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setTraineeUsername("john.doe");

        List<Training> expectedList = List.of(validTraining);
        when(trainingDao.findTraineeTrainingsByCriteria(filter)).thenReturn(expectedList);

        List<Training> result = trainingService.getTraineeTrainingsByCriteria(filter);

        assertEquals(expectedList, result);
        verify(trainingDao, times(1)).findTraineeTrainingsByCriteria(filter);
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldThrowException_whenUsernameIsMissing() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setTraineeUsername("   ");

        assertThrows(IllegalArgumentException.class, () -> trainingService.getTraineeTrainingsByCriteria(filter));
        verifyNoInteractions(trainingDao);
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldReturnList_whenFilterIsValid() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        filter.setTrainerUsername("jane.smith");

        List<Training> expectedList = List.of(validTraining);
        when(trainingDao.findTrainerTrainingsByCriteria(filter)).thenReturn(expectedList);

        List<Training> result = trainingService.getTrainerTrainingsByCriteria(filter);

        assertEquals(expectedList, result);
        verify(trainingDao, times(1)).findTrainerTrainingsByCriteria(filter);
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldThrowException_whenUsernameIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainerTrainingsByCriteria(null));
        verifyNoInteractions(trainingDao);
    }

    @Test
    void findById_shouldReturnTraining() {
        when(trainingDao.findById(10L)).thenReturn(Optional.of(validTraining));
        assertEquals(validTraining, trainingService.findById(10L));
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(trainingDao.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainingService.findById(99L));
    }

    @Test
    void findAll_shouldDelegateToDao() {
        List<Training> expectedList = List.of(validTraining);
        when(trainingDao.findAll()).thenReturn(expectedList);

        assertEquals(expectedList, trainingService.findAll());
        verify(trainingDao, times(1)).findAll();
    }
}
