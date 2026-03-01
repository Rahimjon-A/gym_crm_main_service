package epam.com.gym.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingFilter {
    private String traineeUsername;
    private Date fromDate;
    private Date toDate;
    private String trainerName;
    private String trainingTypeName;
    private Double duration;
    private String trainingName;
}
