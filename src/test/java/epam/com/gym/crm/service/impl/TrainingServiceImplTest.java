package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDao;

    @Mock
    private TrainerDAO trainerDao;

    @Mock
    private TraineeDAO traineeDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO dto;
    private Training training;

    @BeforeEach
    void setup() {
        dto = new TrainingDTO();
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingName("Morning Cardio");
        dto.setTrainingType(TrainingType.CARDIO);
        dto.setTrainingDate(LocalDate.now());
        dto.setTrainingDuration(60.0);

        training = new Training();
        training.setId(10L);
        training.setTrainerId(1L);
        training.setTraineeId(2L);
        training.setTrainingName("Morning Cardio");
        training.setTrainingType(TrainingType.CARDIO);
        training.setTrainingDate(dto.getTrainingDate());
        training.setTrainingDuration(60.);
    }

    @Test
    void create_shouldSaveTrainingWhenTrainerAndTraineeExist() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(mock()));
        when(traineeDao.findById(2L)).thenReturn(Optional.of(mock()));
        when(trainingDao.save(any(Training.class))).thenReturn(training);

        Training result = trainingService.create(dto);

        assertNotNull(result);
        assertEquals("Morning Cardio", result.getTrainingName());
        verify(trainerDao).findById(1L);
        verify(traineeDao).findById(2L);
        verify(trainingDao).save(any(Training.class));
    }

    @Test
    void create_shouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.create(dto));

        verify(traineeDao, never()).findById(any());
        verify(trainingDao, never()).save(any());
    }

    @Test
    void create_shouldThrowExceptionWhenTraineeNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(mock()));
        when(traineeDao.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.create(dto));

        verify(trainingDao, never()).save(any());
    }

    @Test
    void findById_shouldReturnTrainingWhenExists() {
        when(trainingDao.findById(10L)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.findById(10L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        when(trainingDao.findById(10L)).thenReturn(Optional.empty());

        Optional<Training> result = trainingService.findById(10L);

        assertTrue(result.isEmpty());
    }


    @Test
    void findAll_shouldReturnList() {
        when(trainingDao.findAll()).thenReturn(List.of(training));

        List<Training> result = trainingService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(trainingDao.findAll()).thenReturn(Collections.emptyList());

        List<Training> result = trainingService.findAll();

        assertTrue(result.isEmpty());
    }
}
