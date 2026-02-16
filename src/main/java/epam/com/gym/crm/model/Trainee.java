package epam.com.gym.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainee extends User {
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}