package epam.com.gym.crm.actuator;

import epam.com.gym.crm.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeReferenceDataHealthIndicator implements HealthIndicator {

    private static final String KEY_TRAINING_TYPES_COUNT = "trainingTypesCount";
    private static final String KEY_MESSAGE = "message";
    
    private static final String MSG_DATA_OK = "Core reference data is populated and ready.";
    private static final String MSG_DATA_MISSING = "Critical reference data (Training Types) is missing from the database!";

    private TrainingTypeService trainingTypeService;

    @Autowired
    public void setTrainingTypeService(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    public Health health() {
        int trainingTypeCount = trainingTypeService.findAll().size();

        if (trainingTypeCount > 0) {
            return Health.up()
                    .withDetail(KEY_TRAINING_TYPES_COUNT, trainingTypeCount)
                    .withDetail(KEY_MESSAGE, MSG_DATA_OK)
                    .build();
        } else {
            return Health.down()
                    .withDetail(KEY_TRAINING_TYPES_COUNT, trainingTypeCount)
                    .withDetail(KEY_MESSAGE, MSG_DATA_MISSING)
                    .build();
        }
    }
}