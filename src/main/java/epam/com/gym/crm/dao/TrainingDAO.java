package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDAO {
    Training save(Training training);
    Optional<Training> findById(Long id);
    List<Training> findAll();
    void delete(Long id);
}