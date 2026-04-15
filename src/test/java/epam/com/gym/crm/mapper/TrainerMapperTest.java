package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.dto.response.trainer.TrainerResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerUpdateResponse;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainerMapperTest {
    private static final String USERNAME = "jane.smith";
    private static final String FIRST_NAME = "Jane";
    private static final String LAST_NAME = "Smith";
    private static final String TRAINING_TYPE_NAME = "ZUMBA";
    private static final String TRAINEE_USER = "john.doe";
    private static final Long SPECIALIZATION_ID = 5L;

    private TrainerMapper trainerMapper;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerMapper = new TrainerMapper();

        TrainingType type = new TrainingType();
        type.setTrainingTypeName(TRAINING_TYPE_NAME);

        Trainee trainee = new Trainee();
        trainee.setUsername(TRAINEE_USER);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        Training training1 = new Training();
        training1.setTrainee(trainee);

        Training training2 = new Training();
        training2.setTrainee(trainee);

        trainer = new Trainer();
        trainer.setUsername(USERNAME);
        trainer.setFirstName(FIRST_NAME);
        trainer.setLastName(LAST_NAME);
        trainer.setActive(true);
        trainer.setSpecialization(type);
        trainer.setTrainings(List.of(training1, training2));
    }

    @Test
    void toProfileResponse_shouldMapFieldsAndFilterDistinctTrainees() {
        TrainerResponse response = trainerMapper.toProfileResponse(trainer);

        assertNotNull(response);
        assertEquals(FIRST_NAME, response.getFirstName());
        assertEquals(LAST_NAME, response.getLastName());
        assertTrue(response.getIsActive());
        assertEquals(TRAINING_TYPE_NAME, response.getSpecialization());

        assertNotNull(response.getTrainees());
        assertEquals(1, response.getTrainees().size());
        assertEquals(TRAINEE_USER, response.getTrainees().get(0).getUsername());
    }

    @Test
    void toUpdateResponse_shouldMapFieldsIncludingUsername() {
        TrainerUpdateResponse response = trainerMapper.toUpdateResponse(trainer);

        assertNotNull(response);
        assertEquals(USERNAME, response.getUsername());
        assertEquals(FIRST_NAME, response.getFirstName());
        assertEquals(TRAINING_TYPE_NAME, response.getSpecialization());
        assertEquals(1, response.getTrainees().size());
    }

    @Test
    void toShortDTOList_shouldMapListOfTrainers() {
        List<TrainerShortResponse> result = trainerMapper.toShortDTOList(List.of(trainer));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(USERNAME, result.get(0).getUsername());
        assertEquals(FIRST_NAME, result.get(0).getFirstName());
        assertEquals(LAST_NAME, result.get(0).getLastName());
        assertEquals(TRAINING_TYPE_NAME, result.get(0).getSpecialization());
    }

    @Test
    void toEntity_fromCreateRequest_shouldMapFieldsAndTrimNames() {
        TrainerCreateRequest request = new TrainerCreateRequest();
        request.setFirstName("   " + FIRST_NAME + "  ");
        request.setLastName(" " + LAST_NAME + " ");
        request.setIsActive(true);
        request.setSpecializationId(SPECIALIZATION_ID);

        Trainer result = trainerMapper.toEntity(request);

        assertNotNull(result);
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertTrue(result.isActive());

        assertNotNull(result.getSpecialization());
        assertEquals(SPECIALIZATION_ID, result.getSpecialization().getId());
    }

    @Test
    void toEntity_fromCreateRequest_shouldHandleNullFields() {
        TrainerCreateRequest request = new TrainerCreateRequest();
        request.setIsActive(false);

        Trainer result = trainerMapper.toEntity(request);

        assertNotNull(result);
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertFalse(result.isActive());
        assertNull(result.getSpecialization());
    }

    @Test
    void toEntity_fromUpdateRequest_shouldMapFieldsAndTrimNames() {
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        request.setFirstName(FIRST_NAME + "   ");
        request.setLastName("   " + LAST_NAME);
        request.setIsActive(false);
        request.setSpecializationId(SPECIALIZATION_ID);

        Trainer result = trainerMapper.toEntity(request);

        assertNotNull(result);
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertFalse(result.isActive());

        assertNotNull(result.getSpecialization());
        assertEquals(SPECIALIZATION_ID, result.getSpecialization().getId());
    }

    @Test
    void toEntity_fromUpdateRequest_shouldHandleNullFields() {
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        request.setIsActive(true);

        Trainer result = trainerMapper.toEntity(request);

        assertNotNull(result);
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertTrue(result.isActive());
        assertNull(result.getSpecialization());
    }
}
