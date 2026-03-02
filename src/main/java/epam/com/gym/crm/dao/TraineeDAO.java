package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO extends BaseDAO<Trainee> {
    Optional<Trainee> findByUsername(String username);
}
