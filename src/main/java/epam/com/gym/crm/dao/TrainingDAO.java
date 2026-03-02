package epam.com.gym.crm.dao;

import epam.com.gym.crm.filter.TraineeTrainingFilter;
import epam.com.gym.crm.filter.TrainerTrainingFilter;
import epam.com.gym.crm.model.Training;

import java.util.List;

public interface TrainingDAO extends BaseDAO<Training> {
    List<Training> findTraineeTrainingsByCriteria(TraineeTrainingFilter request);
    List<Training> findTrainerTrainingsByCriteria(TrainerTrainingFilter request);
}

