package epam.com.gym.crm.dto.trainee;

import epam.com.gym.crm.dto.BaseUserDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TraineeResponseDTO extends BaseUserDTO {
    private Date dateOfBirth;
    private String address;
    private List<TrainerShortDTO> trainers;

    public TraineeResponseDTO(String firstName, String lastName, Date dateOfBirth,
                              String address, Boolean isActive, List<TrainerShortDTO> trainers) {
        super(firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.trainers = trainers;
    }
}
