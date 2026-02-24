package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.storage.impl.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class TraineeDaoImpl implements UserDAO<Trainee> {

    private TraineeStorage storage;

    @Override
    public Trainee create(Trainee trainee) {
        return storage.create(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        return storage.update(trainee);
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

    @Override
    public boolean existsByUsername(String username) {
        return storage.existsByUsername(username);
    }

    @Autowired
    public void setStorage(TraineeStorage storage) {
        this.storage = storage;
    }
}
