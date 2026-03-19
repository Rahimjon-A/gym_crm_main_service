package epam.com.gym.crm.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerUpdateRequest extends TrainerCreateRequest {
    @NotBlank(message = "Username is mandatory")
    private String username;

    public TrainerUpdateRequest(String firstName, String lastName, Boolean isActive, Long specializationId, String username) {
        super(firstName, lastName, isActive, specializationId);
        this.username = username;
    }
}
