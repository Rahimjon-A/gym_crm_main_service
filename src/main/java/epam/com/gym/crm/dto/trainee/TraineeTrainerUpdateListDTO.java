package epam.com.gym.crm.dto.trainee;

import epam.com.gym.crm.dto.trainer.TrainerAssignmentDTO;
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
public class TraineeTrainerUpdateListDTO {

    @NotEmpty(message = "Assignments list cannot be empty")
    @Valid
    private List<TrainerAssignmentDTO> assignments;
}
