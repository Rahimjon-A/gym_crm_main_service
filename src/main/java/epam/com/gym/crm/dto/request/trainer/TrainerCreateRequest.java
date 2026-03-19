package epam.com.gym.crm.dto.request.trainer;

import epam.com.gym.crm.dto.BaseUserPayload;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TrainerCreateRequest extends BaseUserPayload {
    private Long specializationId;

    public TrainerCreateRequest(String firstName, String lastName, Boolean isActive, Long specializationId) {
        super(firstName, lastName, isActive);
        this.specializationId = specializationId;
    }
}
