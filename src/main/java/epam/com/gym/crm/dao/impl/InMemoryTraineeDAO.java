package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.storage.TraineeStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class InMemoryTraineeDAO implements TraineeDAO {

    private final TraineeStorage storage;

    public InMemoryTraineeDAO(TraineeStorage storage) {
        this.storage = storage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        return storage.save(trainee);
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return storage.findById(id);
    }

    @Override
    public List<Trainee> findAll() {
        return storage.findAll();
    }

    @Override
    public void delete(Long id) {
        storage.delete(id);
    }
}
