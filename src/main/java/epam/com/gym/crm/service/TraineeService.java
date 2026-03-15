package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.model.Trainee;

public interface TraineeService extends AbstractUserService<Trainee, TraineeDTO>{
    void deleteByUsername(String username);
}
