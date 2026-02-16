package epam.com.gym.crm.dto;

import epam.com.gym.crm.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
}