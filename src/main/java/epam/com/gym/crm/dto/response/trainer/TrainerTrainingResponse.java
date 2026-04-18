package epam.com.gym.crm.dto.response.trainer;

import epam.com.gym.crm.dto.response.training.BaseTrainingResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainerTrainingResponse extends BaseTrainingResponse {
    private String traineeName;

    public TrainerTrainingResponse(Long id, String trainingName, Date trainingDate, String trainingType, int trainingDuration, String traineeName) {
        super(id, trainingName, trainingDate, trainingType, trainingDuration);
        this.traineeName = traineeName;
    }
}
