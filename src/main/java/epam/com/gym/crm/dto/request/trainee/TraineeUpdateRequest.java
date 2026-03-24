package epam.com.gym.crm.dto.request.trainee;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TraineeUpdateRequest extends TraineeCreateRequest {
    @NotBlank(message = "Username is mandatory")
    private String username;

    public TraineeUpdateRequest(String firstName, String lastName, Boolean isActive, Date dateOfBirth, String address, String username) {
        super(firstName, lastName, isActive, dateOfBirth, address);
        this.username = username;
    }
}
