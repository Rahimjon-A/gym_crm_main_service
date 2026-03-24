package epam.com.gym.crm.mapper;

import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.dto.response.trainee.TraineeTrainingResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerTrainingResponse;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
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

    public Training toEntity(TrainingCreateRequest request) {
        Training training = new Training();
        training.setTrainingName(request.getTrainingName() != null ? request.getTrainingName().trim() : null);
        training.setTrainingDate(request.getTrainingDate());
        training.setTrainingDuration(request.getTrainingDuration());

        if (request.getTraineeUsername() != null) {
            Trainee dummyTrainee = new Trainee();
            dummyTrainee.setUsername(request.getTraineeUsername());
            training.setTrainee(dummyTrainee);
        }

        if (request.getTrainerUsername() != null) {
            Trainer dummyTrainer = new Trainer();
            dummyTrainer.setUsername(request.getTrainerUsername());
            training.setTrainer(dummyTrainer);
        }

        return training;
    }

    public List<Training> toTrainingEntityList(List<TrainerAssignmentRequest> requests) {
        if (requests == null) return java.util.Collections.emptyList();

        return requests.stream().map(req -> {
            Training training = new Training();
            training.setId(req.getTrainingId());

            Trainer dummyTrainer = new Trainer();
            dummyTrainer.setUsername(req.getNewTrainerUsername());
            training.setTrainer(dummyTrainer);

            return training;
        }).toList();
    }
}
