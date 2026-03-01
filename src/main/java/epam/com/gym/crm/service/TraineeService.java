package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;

import java.util.List;
import java.util.Map;

public interface TraineeService extends BaseService<Trainee, TraineeDTO>{
    Trainee update(Long traineeId, TraineeDTO dto);
    List<Trainer> getUnassignedTrainers(String traineeUsername);
    Trainee findByUsername(String username);
    void deleteByUsername(String username);
    Trainee updateTraineeTrainings(Long traineeId, Map<Long, Long> trainingAndTrainerIds);
}
