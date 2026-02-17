package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.storage.impl.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDaoImpl implements UserDAO<Trainer> {

    private TrainerStorage storage;

    @Override
    public Trainer save(Trainer trainer) {
        return storage.save(trainer);
    }

    @Override
    public Trainer update(Trainer trainer) {
        return storage.update(trainer);
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

    @Autowired
    public void setStorage(TrainerStorage storage) {
        this.storage = storage;
    }
}
