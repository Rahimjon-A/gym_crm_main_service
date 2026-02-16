package epam.com.gym.crm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserDTO {
    private String firstName;
    private String lastName;
}
