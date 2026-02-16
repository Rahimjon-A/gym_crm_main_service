package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTrainerDAO implements TrainerDAO {
    private Map<Long, Trainer> storage;
    private final AtomicLong idSeq = new AtomicLong(1);

    @Autowired
    @Qualifier("trainerStorage")
    public void setStorage(Map<Long, Trainer> storage) {
        this.storage = storage;
        storage.keySet().stream().mapToLong(k -> k).max().ifPresent(max -> idSeq.set(max + 1));
    }

    @Override
    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            trainer.setId(idSeq.getAndIncrement());
        }
        storage.put(trainer.getId(), trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}