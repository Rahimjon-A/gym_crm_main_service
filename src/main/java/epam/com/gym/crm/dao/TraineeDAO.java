package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {
    Trainee save(Trainee trainee);
    Optional<Trainee> findById(Long id);
    List<Trainee> findAll();
    void delete(Long id);
}