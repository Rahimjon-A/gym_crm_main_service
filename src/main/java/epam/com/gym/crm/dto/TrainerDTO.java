package epam.com.gym.crm.dto;

import epam.com.gym.crm.model.TrainingType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TrainerDTO extends BaseUserDTO {
    private TrainingType specialization;

    public TrainerDTO(String firstName, String lastName, TrainingType specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }
}
