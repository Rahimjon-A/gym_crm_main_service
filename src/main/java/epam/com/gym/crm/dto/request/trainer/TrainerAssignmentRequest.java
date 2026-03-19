package epam.com.gym.crm.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerAssignmentRequest {
    
    @NotNull(message = "Training ID is mandatory")
    private Long trainingId;

    @NotBlank(message = "New Trainer Username is mandatory")
    private String newTrainerUsername;
}
