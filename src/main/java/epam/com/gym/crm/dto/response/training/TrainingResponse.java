package epam.com.gym.crm.dto.response.training;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingResponse {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerIsActive;
    private Date trainingDate;
    private Double trainingDuration;
}