package epam.com.gym.crm.filter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingFilter extends BaseTrainingFilter {
    private String trainerUsername;
    private String traineeName;
    private String traineeAddress;
}
