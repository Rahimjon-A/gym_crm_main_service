package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTrainingDAO implements TrainingDAO {
    private Map<Long, Training> storage;
    private final AtomicLong idSeq = new AtomicLong(1);

    @Autowired
    @Qualifier("trainingStorage")
    public void setStorage(Map<Long, Training> storage) {
        this.storage = storage;
        storage.keySet().stream().mapToLong(k -> k).max().ifPresent(max -> idSeq.set(max + 1));
    }

    @Override
    public Training save(Training training) {
        if (training.getId() == null) {
            training.setId(idSeq.getAndIncrement());
        }
        storage.put(training.getId(), training);
        return training;
    }

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}