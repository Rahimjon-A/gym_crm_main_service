package epam.com.gym.crm.dto.response.trainee;

import epam.com.gym.crm.dto.response.training.BaseTrainingResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TraineeTrainingResponse extends BaseTrainingResponse {
    private String trainerName;

    public TraineeTrainingResponse(Long id, String trainingName, Date trainingDate, String trainingType, int trainingDuration, String trainerName) {
        super(id, trainingName, trainingDate, trainingType, trainingDuration);
        this.trainerName = trainerName;
    }
}
