package epam.com.gym.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "trainings")
@Getter
@Setter
@NoArgsConstructor
public class Training extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;

    @Temporal(TemporalType.DATE)
    @Column(name = "training_date", nullable = false)
    private Date trainingDate;

    @Column(name = "training_duration", nullable = false)
    private int trainingDuration;
}
