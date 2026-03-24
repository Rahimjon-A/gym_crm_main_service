package epam.com.gym.crm.dto.response.trainer;

import epam.com.gym.crm.dto.response.UserShortResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerShortResponse extends UserShortResponse {
    private String specialization;

    public TrainerShortResponse(String username, String firstName, String lastName, String specialization) {
        super(username, firstName, lastName);
        this.specialization = specialization;
    }
}

