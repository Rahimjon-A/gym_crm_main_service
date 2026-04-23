package epam.com.gym.crm.model.common;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerWorkloadMessage {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Date trainingDate;
    private int trainingDuration;
    private ActionType actionType;

    public enum ActionType {
        ADD, DELETE
    }
}
