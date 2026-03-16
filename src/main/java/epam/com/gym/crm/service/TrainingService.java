package epam.com.gym.crm.service;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.model.Training;

import java.util.List;
import java.util.Map;

public interface TrainingService extends BaseService<Training, TrainingDTO> {
    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter);
    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter);
    List<Training> updateTraineeTrainings(Long traineeId, Map<Long, Long> trainingAndTrainerIds);
}
