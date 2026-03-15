package epam.com.gym.crm.dto.trainer;

import epam.com.gym.crm.dto.trainee.TraineeShortDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainerUpdateDTO extends TrainerResponseDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;

    public TrainerUpdateDTO(String username, String firstName, String lastName, Boolean isActive, 
                            String specialization, List<TraineeShortDTO> trainees) {
        super(firstName, lastName, isActive, specialization, trainees);
        this.username = username;
    }
}
