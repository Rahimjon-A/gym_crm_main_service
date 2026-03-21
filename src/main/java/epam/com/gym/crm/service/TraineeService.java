package epam.com.gym.crm.service;

import epam.com.gym.crm.model.Trainee;

public interface TraineeService extends AbstractUserService<Trainee>{
    void deleteByUsername(String username);
}
