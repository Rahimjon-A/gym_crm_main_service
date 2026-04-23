package epam.com.gym.crm.dto.request.training;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateRequest {
    @NotBlank(message = "Trainee username is mandatory")
    private String traineeUsername;

    @NotBlank(message = "Trainer username is mandatory")
    private String trainerUsername;

    @NotBlank(message = "Training name is mandatory")
    private String trainingName;

    @NotNull(message = "Training date is mandatory")
    private Date trainingDate;

    @NotNull(message = "Training duration is mandatory")
    @Positive(message = "Duration must be positive")
    private int trainingDuration;
}
