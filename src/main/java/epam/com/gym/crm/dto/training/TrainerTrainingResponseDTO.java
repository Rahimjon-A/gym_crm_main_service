package epam.com.gym.crm.dto.training;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainerTrainingResponseDTO extends TrainingResponseDTO {
    private String traineeName;

    public TrainerTrainingResponseDTO(Long id, String trainingName, Date trainingDate, String trainingType, Double trainingDuration, String traineeName) {
        super(id, trainingName, trainingDate, trainingType, trainingDuration);
        this.traineeName = traineeName;
    }
}
