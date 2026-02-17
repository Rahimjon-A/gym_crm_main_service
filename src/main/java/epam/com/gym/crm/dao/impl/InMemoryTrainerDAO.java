package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.storage.TrainerStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTrainerDAO implements TrainerDAO {

    private final TrainerStorage storage;

    public InMemoryTrainerDAO(TrainerStorage storage) {
        this.storage = storage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        return storage.save(trainer);
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return storage.findById(id);
    }

    @Override
    public List<Trainer> findAll() {
        return storage.findAll();
    }

    @Override
    public void delete(Long id) {
        storage.delete(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return storage.existsByUsername(username);
    }
}
