package epam.com.gym.crm.service;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.model.Training;

import java.util.List;

public interface TrainingService extends BaseService<Training, TrainingCreateRequest> {
    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter);
    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter);
    List<Training> updateTraineeTrainings(String traineeUsername, List<TrainerAssignmentRequest> assignments);
}
