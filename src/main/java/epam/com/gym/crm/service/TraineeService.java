package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;

import java.util.List;
import java.util.Map;

public interface TraineeService extends AbstractUserService<Trainee, TraineeDTO>{
    void deleteByUsername(String username);
}
