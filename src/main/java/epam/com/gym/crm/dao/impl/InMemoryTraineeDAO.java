package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTraineeDAO implements TraineeDAO {
    private Map<Long, Trainee> storage;
    private final AtomicLong idSeq = new AtomicLong(1);

    @Autowired
    @Qualifier("traineeStorage")
    public void setStorage(Map<Long, Trainee> storage) {
        this.storage = storage;
        storage.keySet().stream().mapToLong(k -> k).max().ifPresent(max -> idSeq.set(max + 1));
    }

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            trainee.setId(idSeq.getAndIncrement());
        }
        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}