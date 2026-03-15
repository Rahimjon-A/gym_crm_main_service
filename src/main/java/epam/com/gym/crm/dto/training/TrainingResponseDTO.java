package epam.com.gym.crm.dto.training;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class TrainingResponseDTO {
    private Long trainingId;
    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private Double trainingDuration;
}
