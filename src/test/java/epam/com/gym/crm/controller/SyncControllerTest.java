package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.response.training.TrainingWorkloadResponse;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainingMapper;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncControllerTest {
    private static final String TRAINER_USERNAME = "jane.smith";
    private static final String TRAINEE_USERNAME = "john.doe";
    private static final String TRAINING_NAME = "Morning Cardio";
    private static final Double TRAINING_DURATION = 60.0;

    @Mock
    private GymFacade gymFacade;
    @Mock private TrainingMapper trainingMapper;

    @InjectMocks
    private SyncController syncController;

    private Training training;
    private TrainingWorkloadResponse trainingWorkloadResponse;

    @BeforeEach
    void setUp() {
        Trainer trainer = new Trainer();
        trainer.setUsername(TRAINER_USERNAME);

        Trainee trainee = new Trainee();
        trainee.setUsername(TRAINEE_USERNAME);

        training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName(TRAINING_NAME);
        training.setTrainingDuration(TRAINING_DURATION);
        training.setTrainingDate(new Date());

        trainingWorkloadResponse = new TrainingWorkloadResponse();
        trainingWorkloadResponse.setTrainerUsername(TRAINER_USERNAME);
        trainingWorkloadResponse.setTrainingDuration(TRAINING_DURATION);
    }

    @Test
    void getAllTrainings_shouldReturnMappedList_whenTrainingsExist() {
        List<Training> trainings = List.of(training);
        List<TrainingWorkloadResponse> responses = List.of(trainingWorkloadResponse);

        when(gymFacade.getAllTrainings()).thenReturn(trainings);
        when(trainingMapper.toTrainingResponse(trainings)).thenReturn(responses);

        ResponseEntity<List<TrainingWorkloadResponse>> result = syncController.getAllTrainings();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(TRAINER_USERNAME, result.getBody().get(0).getTrainerUsername());

        verify(gymFacade).getAllTrainings();
        verify(trainingMapper).toTrainingResponse(trainings);
    }

    @Test
    void getAllTrainings_shouldReturnEmptyList_whenNoTrainingsExist() {
        when(gymFacade.getAllTrainings()).thenReturn(List.of());
        when(trainingMapper.toTrainingResponse(List.of())).thenReturn(List.of());

        ResponseEntity<List<TrainingWorkloadResponse>> result = syncController.getAllTrainings();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }
}
