package epam.com.gym.crm.dto.trainee;

import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TraineeUpdateDTO extends TraineeResponseDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;

    public TraineeUpdateDTO(String username, String firstName, String lastName, Date dateOfBirth,
                            String address, Boolean isActive, List<TrainerShortDTO> trainers) {
        super(firstName, lastName, dateOfBirth, address, isActive, trainers);
        this.username = username;
    }
}
