package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.storage.TrainingStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTrainingDAO implements TrainingDAO {

    private final TrainingStorage storage;

    public InMemoryTrainingDAO(TrainingStorage storage) {
        this.storage = storage;
    }

    @Override
    public Training save(Training training) {
        return storage.save(training);
    }

    @Override
    public Optional<Training> findById(Long id) {
        return storage.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return storage.findAll();
    }

    @Override
    public void delete(Long id) {
        storage.delete(id);
    }
}
