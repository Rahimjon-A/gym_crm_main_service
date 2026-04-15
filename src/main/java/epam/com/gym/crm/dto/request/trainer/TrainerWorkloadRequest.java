package epam.com.gym.crm.dto.request.trainer;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerWorkloadRequest {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Date trainingDate;
    private Double trainingDuration;
}