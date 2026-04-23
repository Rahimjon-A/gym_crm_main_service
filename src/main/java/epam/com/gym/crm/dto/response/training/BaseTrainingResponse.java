package epam.com.gym.crm.dto.response.training;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseTrainingResponse {
    private Long trainingId;
    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private int trainingDuration;
}
