package epam.com.gym.crm.dao.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseTrainingFilter {
    private String username;
    private Date fromDate;
    private Date toDate;
}

