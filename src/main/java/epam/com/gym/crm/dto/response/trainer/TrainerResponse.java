package epam.com.gym.crm.dto.response.trainer;

import epam.com.gym.crm.dto.BaseUserPayload;
import epam.com.gym.crm.dto.response.trainee.TraineeShortResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainerResponse extends BaseUserPayload {
    private String specialization;
    private List<TraineeShortResponse> trainees;

    public TrainerResponse(String firstName, String lastName, Boolean isActive,
                           String specialization, List<TraineeShortResponse> trainees) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
        this.trainees = trainees;
    }
}
