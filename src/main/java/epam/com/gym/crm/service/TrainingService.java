package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TraineeTrainingFilter;
import epam.com.gym.crm.dto.TrainerTrainingFilter;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.model.Training;

import java.util.List;

public interface TrainingService extends BaseService<Training, TrainingDTO> {
    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter);
    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter);
}
