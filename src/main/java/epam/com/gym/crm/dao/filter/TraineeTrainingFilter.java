package epam.com.gym.crm.dao.filter;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class TraineeTrainingFilter extends BaseTrainingFilter {
    private String trainingTypeName;
    private String trainerName;

    public TraineeTrainingFilter(String username, Date fromDate, Date toDate, String trainingTypeName, String trainerName) {
        super(username, fromDate, toDate);
        this.trainingTypeName = trainingTypeName;
        this.trainerName = trainerName;
    }
}
