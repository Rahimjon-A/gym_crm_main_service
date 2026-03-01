package epam.com.gym.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingFilter {
    private String trainerUsername;
    private Date fromDate;
    private Date toDate;
    private String trainingName;
    private String traineeName;
    private String traineeAddress;
    private Double duration;
}
