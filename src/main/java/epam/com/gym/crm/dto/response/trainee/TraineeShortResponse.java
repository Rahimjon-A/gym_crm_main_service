package epam.com.gym.crm.dto.response.trainee;

import epam.com.gym.crm.dto.response.UserShortResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraineeShortResponse extends UserShortResponse {

    public TraineeShortResponse(String username, String firstName, String lastName) {
        super(username, firstName, lastName);
    }
}
