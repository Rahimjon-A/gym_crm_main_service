package epam.com.gym.crm.dao.filter;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainerTrainingFilter extends BaseTrainingFilter {
    private String traineeName;

    public TrainerTrainingFilter(String username, Date fromDate, Date toDate, String traineeName) {
        super(username, fromDate, toDate);
        this.traineeName = traineeName;
    }
}
