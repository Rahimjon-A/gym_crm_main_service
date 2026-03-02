package epam.com.gym.crm.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseTrainingFilter {
    private Date fromDate;
    private Date toDate;
    private Double duration;
    private String trainingName;
}
