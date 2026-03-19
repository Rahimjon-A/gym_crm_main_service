package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.response.trainee.TraineeTrainingResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerTrainingResponse;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperTest {

    private static final String TRAINING_NAME = "Afternoon Yoga";
    private static final String TRAINING_TYPE = "YOGA";
    private static final Double DURATION = 90.0;
    private static final String TRAINEE_FIRST_NAME = "Alice";
    private static final String TRAINER_FIRST_NAME = "Bob";

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
}
