package epam.com.gym.crm.dto.response.trainer;

import epam.com.gym.crm.dto.response.trainee.TraineeShortResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainerUpdateResponse extends TrainerResponse {
    @NotBlank(message = "Username is mandatory")
    private String username;

    public TrainerUpdateResponse(String username, String firstName, String lastName, Boolean isActive,
                                 String specialization, List<TraineeShortResponse> trainees) {
        super(firstName, lastName, isActive, specialization, trainees);
        this.username = username;
    }
}
