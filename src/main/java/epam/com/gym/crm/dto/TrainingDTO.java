package epam.com.gym.crm.dto;

import epam.com.gym.crm.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private Long trainingTypeId;
    private Date trainingDate;
    private Double trainingDuration;
}
