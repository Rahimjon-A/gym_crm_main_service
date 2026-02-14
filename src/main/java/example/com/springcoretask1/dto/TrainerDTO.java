package example.com.springcoretask1.dto;

import example.com.springcoretask1.model.TrainingType;
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