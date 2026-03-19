package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.model.Trainee;

public interface TraineeService extends AbstractUserService<Trainee, TraineeCreateRequest>{
    void deleteByUsername(String username);
}
