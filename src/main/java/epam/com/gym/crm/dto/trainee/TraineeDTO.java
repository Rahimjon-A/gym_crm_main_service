package epam.com.gym.crm.dto.trainee;

import epam.com.gym.crm.dto.BaseUserDTO;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class TraineeDTO extends BaseUserDTO {
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;
    private String address;
}
