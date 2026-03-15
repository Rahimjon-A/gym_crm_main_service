package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.training.TraineeTrainingResponseDTO;
import epam.com.gym.crm.dto.training.TrainerTrainingResponseDTO;
import epam.com.gym.crm.dto.training.TrainingResponseDTO;
import epam.com.gym.crm.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingMapper {

    public List<TrainerTrainingResponseDTO> mapTrainerTrainings(List<Training> trainings) {
        return trainings.stream()
                .map(training -> new TrainerTrainingResponseDTO(
                        training.getId(),
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainee().getFirstName()
                ))
                .toList();
    }

    public List<TraineeTrainingResponseDTO> mapTraineeTrainings(List<Training> trainings) {
        return trainings.stream()
                .map(training -> new TraineeTrainingResponseDTO(
                        training.getId(),
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainer().getFirstName()
                ))
                .toList();
    }
}
