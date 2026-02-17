package epam.com.gym.crm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class TraineeDTO extends BaseUserDTO {
    private LocalDate dateOfBirth;
    private String address;

    public TraineeDTO(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
