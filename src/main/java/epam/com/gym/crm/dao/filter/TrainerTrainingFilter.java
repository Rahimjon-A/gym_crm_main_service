package epam.com.gym.crm.dao.filter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingFilter extends BaseTrainingFilter {
    private String trainerUsername;
    private String traineeName;
}
