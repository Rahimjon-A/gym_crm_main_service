package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDAO {
    Optional<TrainingType> findById(Long id);
    Optional<TrainingType> findByName(String name);
    List<TrainingType> findAll();
    Long count();
}
