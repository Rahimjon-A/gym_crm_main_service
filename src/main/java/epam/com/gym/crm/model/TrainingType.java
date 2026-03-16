package epam.com.gym.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "training_types")
@Getter
@Setter
@NoArgsConstructor
public class TrainingType extends BaseEntity {

    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;
}
