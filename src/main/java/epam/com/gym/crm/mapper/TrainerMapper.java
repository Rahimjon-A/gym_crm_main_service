package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.trainee.TraineeShortDTO;
import epam.com.gym.crm.dto.trainer.TrainerResponseDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import epam.com.gym.crm.dto.trainer.TrainerUpdateDTO;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerMapper {

    public TrainerResponseDTO toProfileResponse(Trainer trainer) {
        return new TrainerResponseDTO(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                trainer.getSpecialization().getTrainingTypeName(),
                extractTrainees(trainer)
        );
    }

    public TrainerUpdateDTO toUpdateResponse(Trainer trainer) {
        return new TrainerUpdateDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                trainer.getSpecialization().getTrainingTypeName(),
                extractTrainees(trainer)
        );
    }

    public List<TrainerShortDTO> toShortDTOList(List<Trainer> trainers) {
        return trainers.stream()
                .map(trainer -> new TrainerShortDTO(
                        trainer.getUsername(),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getSpecialization().getTrainingTypeName()
                ))
                .toList();
    }

    private List<TraineeShortDTO> extractTrainees(Trainer trainer) {
        return trainer.getTrainings().stream()
                .map(Training::getTrainee)
                .distinct()
                .map(trainee -> new TraineeShortDTO(
                        trainee.getUsername(),
                        trainee.getFirstName(),
                        trainee.getLastName()
                ))
                .toList();
    }
}
