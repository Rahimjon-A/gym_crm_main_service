package epam.com.gym.crm.actuator;

import epam.com.gym.crm.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeReferenceDataHealthIndicatorTest {

    private static final String KEY_TRAINING_TYPES_COUNT = "trainingTypesCount";
    private static final String KEY_MESSAGE = "message";
    
    private static final String MSG_DATA_OK = "Core reference data is populated and ready.";
    private static final String MSG_DATA_MISSING = "Critical reference data (Training Types) is missing from the database!";

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeReferenceDataHealthIndicator healthIndicator;

    @Test
    void health_shouldReturnUp_whenDataExists() {
        when(trainingTypeService.count()).thenReturn(1L);

        Health result = healthIndicator.health();

        assert result != null;
        assertEquals(Status.UP, result.getStatus());
        assertEquals(1L, result.getDetails().get(KEY_TRAINING_TYPES_COUNT));
        assertEquals(MSG_DATA_OK, result.getDetails().get(KEY_MESSAGE));
    }

    @Test
    void health_shouldReturnDown_whenDataEmpty() {
        when(trainingTypeService.count()).thenReturn(0L);

        Health result = healthIndicator.health();

        assert result != null;
        assertEquals(Status.DOWN, result.getStatus());
        assertEquals(0L, result.getDetails().get(KEY_TRAINING_TYPES_COUNT));
        assertEquals(MSG_DATA_MISSING, result.getDetails().get(KEY_MESSAGE));
    }
}
