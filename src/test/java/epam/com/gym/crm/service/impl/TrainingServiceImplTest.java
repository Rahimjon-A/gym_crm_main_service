package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.BaseDAO;
import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
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
    private BaseDAO<Training> trainingDao;

    @Mock
    private UserDAO<Trainer> trainerDao;

    @Mock
    private UserDAO<Trainee> traineeDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO dto;

    @BeforeEach
    void setup() {
        dto = new TrainingDTO();
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingName("Morning Cardio");
        dto.setTrainingType(TrainingType.CARDIO);
        dto.setTrainingDate(LocalDate.now());
        dto.setTrainingDuration(60.0);
    }

    @Test
    void create_shouldSaveTrainingWhenTrainerAndTraineeExist() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(mock(Trainer.class)));
        when(traineeDao.findById(2L)).thenReturn(Optional.of(mock(Trainee.class)));
        when(trainingDao.create(any(Training.class))).thenAnswer(invocation -> {
            Training t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });

        Training result = trainingService.create(dto);

        assertNotNull(result);
        assertEquals("Morning Cardio", result.getTrainingName());
        assertEquals(1L, result.getTrainerId());
        assertEquals(2L, result.getTraineeId());
        assertEquals(60.0, result.getTrainingDuration());
        verify(trainerDao).findById(1L);
        verify(traineeDao).findById(2L);
        verify(trainingDao).create(any(Training.class));
    }

    @Test
    void create_shouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(dto));

        verify(traineeDao, never()).findById(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void create_shouldThrowExceptionWhenTraineeNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(mock(Trainer.class)));
        when(traineeDao.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(dto));

        verify(trainingDao, never()).create(any());
    }

    @Test
    void findById_shouldReturnTrainingWhenExists() {
        Training training = new Training();
        training.setId(10L);
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
        Training training = new Training();
        training.setId(10L);
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
