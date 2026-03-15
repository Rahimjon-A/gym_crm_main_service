package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.trainee.TraineeResponseDTO;
import epam.com.gym.crm.dto.trainee.TraineeUpdateDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeMapper {

    /**
     * Maps to the GET Profile Response
     */
    public TraineeResponseDTO toProfileResponse(Trainee trainee) {
        return new TraineeResponseDTO(
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                extractTrainers(trainee)
        );
    }

    /**
     * Maps to the PUT Update Response (includes username)
     */
    public TraineeUpdateDTO toUpdateResponse(Trainee trainee) {
        return new TraineeUpdateDTO(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                extractTrainers(trainee)
        );
    }

    public List<TrainerShortDTO> extractTrainers(Trainee trainee) {
        return trainee.getTrainings().stream()
                .map(Training::getTrainer)
                .distinct()
                .map(trainer -> new TrainerShortDTO(
                        trainer.getUsername(),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getSpecialization().getTrainingTypeName()
                ))
                .toList();
    }
}
