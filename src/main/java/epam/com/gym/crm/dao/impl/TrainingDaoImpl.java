package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.BaseDAO;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.storage.impl.TrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements BaseDAO<Training> {

    private TrainingStorage storage;

    @Override
    public Training create(Training training) {
        return storage.create(training);
    }

    @Override
    public Training update(Training training) {
        return storage.update(training);
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

    @Autowired
    public void setStorage(TrainingStorage storage) {
        this.storage = storage;
    }
}
