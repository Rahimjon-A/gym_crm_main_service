package epam.com.gym.crm.dto.request.trainee;

import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainerUpdateListRequest {

    @NotEmpty(message = "Assignments list cannot be empty")
    @Valid
    private List<TrainerAssignmentRequest> assignments;
}
