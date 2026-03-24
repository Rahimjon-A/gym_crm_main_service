package epam.com.gym.crm.dto.response.trainee;

import epam.com.gym.crm.dto.BaseUserPayload;
import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TraineeResponse extends BaseUserPayload {
    private Date dateOfBirth;
    private String address;
    private List<TrainerShortResponse> trainers;

    public TraineeResponse(String firstName, String lastName, Date dateOfBirth,
                           String address, Boolean isActive, List<TrainerShortResponse> trainers) {
        super(firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.trainers = trainers;
    }
}
