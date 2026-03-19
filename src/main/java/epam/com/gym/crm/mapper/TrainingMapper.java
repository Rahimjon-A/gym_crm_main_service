package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.response.trainee.TraineeTrainingResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerTrainingResponse;
import epam.com.gym.crm.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingMapper {

    public List<TrainerTrainingResponse> mapTrainerTrainings(List<Training> trainings) {
        return trainings.stream()
                .map(training -> new TrainerTrainingResponse(
                        training.getId(),
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainee().getFirstName()
                ))
                .toList();
    }

    public List<TraineeTrainingResponse> mapTraineeTrainings(List<Training> trainings) {
        return trainings.stream()
                .map(training -> new TraineeTrainingResponse(
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
