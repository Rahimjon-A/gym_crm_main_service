package epam.com.gym.crm.dto.training;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TraineeTrainingResponseDTO extends TrainingResponseDTO {
    private String trainerName;

    public TraineeTrainingResponseDTO(Long id, String trainingName, Date trainingDate, String trainingType, Double trainingDuration, String trainerName) {
        super(id, trainingName, trainingDate, trainingType, trainingDuration);
        this.trainerName = trainerName;
    }
}
