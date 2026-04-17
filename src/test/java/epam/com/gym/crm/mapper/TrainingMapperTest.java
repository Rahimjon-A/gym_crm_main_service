package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.dto.response.trainee.TraineeTrainingResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerTrainingResponse;
import epam.com.gym.crm.dto.response.training.TrainingWorkloadResponse;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingMapperTest {

    private static final String TRAINING_NAME = "Afternoon Yoga";
    private static final String TRAINING_TYPE = "YOGA";
    private static final Double DURATION = 90.0;
    private static final String TRAINEE_FIRST_NAME = "Alice";
    private static final String TRAINER_FIRST_NAME = "Bob";
    private static final String TRAINEE_USERNAME = "alice.smith";
    private static final String TRAINER_USERNAME = "bob.jones";
    private static final String TRAINER_LAST_NAME = "Jones";
    private static final Long TRAINING_ID = 1L;

    private TrainingMapper trainingMapper;
    private Training training;
    private Date now;

    @BeforeEach
    void setUp() {
        trainingMapper = new TrainingMapper();
        now = new Date();

        TrainingType type = new TrainingType();
        type.setTrainingTypeName(TRAINING_TYPE);

        Trainee trainee = new Trainee();
        trainee.setFirstName(TRAINEE_FIRST_NAME);

        Trainer trainer = new Trainer();
        trainer.setFirstName(TRAINER_FIRST_NAME);

        training = new Training();
        training.setTrainingName(TRAINING_NAME);
        training.setTrainingDate(now);
        training.setTrainingDuration(DURATION);
        training.setTrainingType(type);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
    }

    @Test
    void mapTrainerTrainings_shouldMapFieldsAndUseTraineeName() {
        List<TrainerTrainingResponse> result = trainingMapper.mapTrainerTrainings(List.of(training));

        assertNotNull(result);
        assertEquals(1, result.size());

        TrainerTrainingResponse dto = result.get(0);
        assertEquals(TRAINING_NAME, dto.getTrainingName());
        assertEquals(now, dto.getTrainingDate());
        assertEquals(TRAINING_TYPE, dto.getTrainingType());
        assertEquals(DURATION, dto.getTrainingDuration());
        
        assertEquals(TRAINEE_FIRST_NAME, dto.getTraineeName());
    }

    @Test
    void mapTraineeTrainings_shouldMapFieldsAndUseTrainerName() {
        List<TraineeTrainingResponse> result = trainingMapper.mapTraineeTrainings(List.of(training));

        assertNotNull(result);
        assertEquals(1, result.size());

        TraineeTrainingResponse dto = result.get(0);
        assertEquals(TRAINING_NAME, dto.getTrainingName());
        assertEquals(now, dto.getTrainingDate());
        assertEquals(TRAINING_TYPE, dto.getTrainingType());
        assertEquals(DURATION, dto.getTrainingDuration());
        
        assertEquals(TRAINER_FIRST_NAME, dto.getTrainerName());
    }

    @Test
    void toEntity_shouldMapAllFieldsAndTrimName() {
        TrainingCreateRequest request = new TrainingCreateRequest();
        request.setTrainingName("   " + TRAINING_NAME + "   ");
        request.setTrainingDate(now);
        request.setTrainingDuration(DURATION);
        request.setTraineeUsername(TRAINEE_USERNAME);
        request.setTrainerUsername(TRAINER_USERNAME);

        Training result = trainingMapper.toEntity(request);

        assertEquals(TRAINING_NAME, result.getTrainingName());
        assertEquals(now, result.getTrainingDate());
        assertEquals(DURATION, result.getTrainingDuration());

        assertNotNull(result.getTrainee());
        assertEquals(TRAINEE_USERNAME, result.getTrainee().getUsername());

        assertNotNull(result.getTrainer());
        assertEquals(TRAINER_USERNAME, result.getTrainer().getUsername());
    }

    @Test
    void toEntity_shouldHandleNullUsernamesAndName() {
        epam.com.gym.crm.dto.request.training.TrainingCreateRequest request = new epam.com.gym.crm.dto.request.training.TrainingCreateRequest();
        request.setTrainingDate(now);
        request.setTrainingDuration(DURATION);

        Training result = trainingMapper.toEntity(request);

        assertNull(result.getTrainingName());
        assertNull(result.getTrainee());
        assertNull(result.getTrainer());
    }

    @Test
    void toTrainingEntityList_shouldMapRequestsToEntities() {
        TrainerAssignmentRequest req = new TrainerAssignmentRequest();
        req.setTrainingId(TRAINING_ID);
        req.setNewTrainerUsername(TRAINER_USERNAME);

        List<Training> result = trainingMapper.toTrainingEntityList(List.of(req));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TRAINING_ID, result.get(0).getId());
        assertNotNull(result.get(0).getTrainer());
        assertEquals(TRAINER_USERNAME, result.get(0).getTrainer().getUsername());
    }

    @Test
    void toTrainingEntityList_shouldReturnEmptyList_whenInputIsNull() {
        List<Training> result = trainingMapper.toTrainingEntityList(null);

        assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void toTrainingResponse_shouldMapTrainerAndTrainingFields() {
        training.getTrainer().setUsername(TRAINER_USERNAME);
        training.getTrainer().setLastName(TRAINER_LAST_NAME);
        training.getTrainer().setActive(true);

        List<TrainingWorkloadResponse> result = trainingMapper.toTrainingResponse(List.of(training));

        assertNotNull(result);
        assertEquals(1, result.size());

        TrainingWorkloadResponse response = result.get(0);
        assertEquals(TRAINER_USERNAME, response.getTrainerUsername());
        assertEquals(TRAINER_FIRST_NAME, response.getTrainerFirstName());
        assertEquals(TRAINER_LAST_NAME, response.getTrainerLastName());
        org.junit.jupiter.api.Assertions.assertTrue(response.isTrainerIsActive());
        assertEquals(now, response.getTrainingDate());
        assertEquals(DURATION, response.getTrainingDuration());
    }
}
