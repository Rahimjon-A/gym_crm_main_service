package epam.com.gym.crm.dto.trainer;

import epam.com.gym.crm.dto.BaseUserDTO;
import epam.com.gym.crm.dto.trainee.TraineeShortDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainerResponseDTO extends BaseUserDTO {
    private String specialization;
    private List<TraineeShortDTO> trainees;

    public TrainerResponseDTO(String firstName, String lastName, Boolean isActive, 
                              String specialization, List<TraineeShortDTO> trainees) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
        this.trainees = trainees;
    }
}
