package epam.com.gym.crm.dao;

import epam.com.gym.crm.dto.TraineeTrainingFilter;
import epam.com.gym.crm.dto.TrainerTrainingFilter;
import epam.com.gym.crm.model.Training;

import java.util.Date;
import java.util.List;

public interface TrainingDAO extends BaseDAO<Training> {
    List<Training> findTraineeTrainingsByCriteria(TraineeTrainingFilter request);
    List<Training> findTrainerTrainingsByCriteria(TrainerTrainingFilter request);
}

