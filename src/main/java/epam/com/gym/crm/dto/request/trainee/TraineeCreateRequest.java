package epam.com.gym.crm.dto.request.trainee;

import epam.com.gym.crm.dto.BaseUserPayload;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class TraineeCreateRequest extends BaseUserPayload {
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;
    private String address;

    public TraineeCreateRequest(String firstName, String lastName, Boolean isActive, Date dateOfBirth, String address) {
        super(firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
