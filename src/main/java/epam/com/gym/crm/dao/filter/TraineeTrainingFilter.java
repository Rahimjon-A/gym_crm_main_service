package epam.com.gym.crm.dao.filter;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingFilter extends BaseTrainingFilter {
    private String traineeUsername;
    private String trainerName;
    private String trainingTypeName;
}
