package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.trainee.TraineeResponseDTO;
import epam.com.gym.crm.dto.trainee.TraineeUpdateDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraineeMapperTest {
    private static final String USERNAME = "john.doe";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String ADDRESS = "123 Main St";
    private static final String TRAINER_USER = "jane.smith";
    private static final String TRAINING_TYPE = "YOGA";

    private TraineeMapper traineeMapper;
    private Trainee trainee;
    private Date dob;

    @BeforeEach
    void setUp() {
        traineeMapper = new TraineeMapper();
        dob = new Date();

        TrainingType type = new TrainingType();
        type.setTrainingTypeName(TRAINING_TYPE);

        Trainer trainer = new Trainer();
        trainer.setUsername(TRAINER_USER);
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        trainer.setSpecialization(type);

        Training training1 = new Training();
        training1.setTrainer(trainer);

        Training training2 = new Training();
        training2.setTrainer(trainer);

        trainee = new Trainee();
        trainee.setUsername(USERNAME);
        trainee.setFirstName(FIRST_NAME);
        trainee.setLastName(LAST_NAME);
        trainee.setDateOfBirth(dob);
        trainee.setAddress(ADDRESS);
        trainee.setActive(true);
        trainee.setTrainings(List.of(training1, training2));
    }

    @Test
    void toProfileResponse_shouldMapAllFieldsAndFilterDistinctTrainers() {
        TraineeResponseDTO response = traineeMapper.toProfileResponse(trainee);

        assertNotNull(response);
        assertEquals(FIRST_NAME, response.getFirstName());
        assertEquals(LAST_NAME, response.getLastName());
        assertEquals(dob, response.getDateOfBirth());
        assertEquals(ADDRESS, response.getAddress());
        assertTrue(response.getIsActive());

        assertNotNull(response.getTrainers());
        assertEquals(1, response.getTrainers().size());
        
        assertEquals(TRAINER_USER, response.getTrainers().get(0).username());
        assertEquals(TRAINING_TYPE, response.getTrainers().get(0).specialization());
    }

    @Test
    void toUpdateResponse_shouldMapAllFieldsIncludingUsername() {
        TraineeUpdateDTO response = traineeMapper.toUpdateResponse(trainee);

        assertNotNull(response);
        assertEquals(USERNAME, response.getUsername());
        assertEquals(FIRST_NAME, response.getFirstName());
        assertEquals(LAST_NAME, response.getLastName());
        assertEquals(ADDRESS, response.getAddress());
        assertTrue(response.getIsActive());
        assertEquals(1, response.getTrainers().size());
    }

    @Test
    void toProfileResponse_shouldHandleEmptyTrainingsList() {
        trainee.setTrainings(List.of());

        TraineeResponseDTO response = traineeMapper.toProfileResponse(trainee);

        assertNotNull(response);
        assertNotNull(response.getTrainers());
        assertTrue(response.getTrainers().isEmpty());
    }
}
