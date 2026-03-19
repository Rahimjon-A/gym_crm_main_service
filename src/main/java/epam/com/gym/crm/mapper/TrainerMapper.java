package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.response.trainee.TraineeShortResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerUpdateResponse;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerMapper {

    public TrainerResponse toProfileResponse(Trainer trainer) {
        return new TrainerResponse(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                trainer.getSpecialization().getTrainingTypeName(),
                extractTrainees(trainer)
        );
    }

    public TrainerUpdateResponse toUpdateResponse(Trainer trainer) {
        return new TrainerUpdateResponse(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                trainer.getSpecialization().getTrainingTypeName(),
                extractTrainees(trainer)
        );
    }

    public List<TrainerShortResponse> toShortDTOList(List<Trainer> trainers) {
        return trainers.stream()
                .map(trainer -> new TrainerShortResponse(
                        trainer.getUsername(),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getSpecialization().getTrainingTypeName()
                ))
                .toList();
    }

    private List<TraineeShortResponse> extractTrainees(Trainer trainer) {
        return trainer.getTrainings().stream()
                .map(Training::getTrainee)
                .distinct()
                .map(trainee -> new TraineeShortResponse(
                        trainee.getUsername(),
                        trainee.getFirstName(),
                        trainee.getLastName()
                ))
                .toList();
    }
}
