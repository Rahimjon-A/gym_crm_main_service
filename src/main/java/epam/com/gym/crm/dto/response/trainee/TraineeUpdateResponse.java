package epam.com.gym.crm.dto.response.trainee;

import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TraineeUpdateResponse extends TraineeResponse {
    @NotBlank(message = "Username is mandatory")
    private String username;

    public TraineeUpdateResponse(String firstName, String lastName, Date dateOfBirth, String address, Boolean isActive, List<TrainerShortResponse> trainers, String username) {
        super(firstName, lastName, dateOfBirth, address, isActive, trainers);
        this.username = username;
    }
}
