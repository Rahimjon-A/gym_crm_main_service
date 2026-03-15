package epam.com.gym.crm.dto.trainer;

import epam.com.gym.crm.dto.BaseUserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TrainerDTO extends BaseUserDTO {
    private Long specializationId;
}
