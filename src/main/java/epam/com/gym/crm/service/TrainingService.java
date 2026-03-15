package epam.com.gym.crm.service;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.trainer.TrainerAssignmentDTO;
import epam.com.gym.crm.dto.training.TrainingDTO;
import epam.com.gym.crm.model.Training;

import java.util.List;
import java.util.Map;

public interface TrainingService extends BaseService<Training, TrainingDTO> {
    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter);
    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter);
    List<Training> updateTraineeTrainings(String traineeUsername, List<TrainerAssignmentDTO> assignments);
}
