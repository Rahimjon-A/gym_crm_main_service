package example.com.springcoretask1.dao.impl;

import example.com.springcoretask1.dao.TraineeDAO;
import example.com.springcoretask1.model.Trainee;
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
        if (trainee.getUserId() == null) {
            trainee.setUserId(idSeq.getAndIncrement());
        }
        storage.put(trainee.getUserId(), trainee);
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