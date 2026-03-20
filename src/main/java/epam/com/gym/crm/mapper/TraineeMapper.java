package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.response.trainee.TraineeResponse;
import epam.com.gym.crm.dto.response.trainee.TraineeUpdateResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer; // Added import
import epam.com.gym.crm.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeMapper {

    public TraineeResponse toProfileResponse(Trainee trainee) {
        return new TraineeResponse(
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                extractTrainers(trainee)
        );
    }

    public TraineeUpdateResponse toUpdateResponse(Trainee trainee) {
        return new TraineeUpdateResponse(
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                extractTrainers(trainee),
                trainee.getUsername()
        );
    }

    public List<TrainerShortResponse> extractTrainers(Trainee trainee) {
        List<Trainer> trainers = trainee.getTrainings().stream()
                .map(Training::getTrainer)
                .distinct()
                .toList();

        return toTrainerShortDTOList(trainers);
    }

    public List<TrainerShortResponse> toTrainerShortDTOList(List<Trainer> trainers) {
        return trainers.stream()
                .map(trainer -> new TrainerShortResponse(
                        trainer.getUsername(),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getSpecialization().getTrainingTypeName()
                ))
                .toList();
    }
}
