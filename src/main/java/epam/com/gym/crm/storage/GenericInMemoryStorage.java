package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GenericInMemoryStorage<T extends BaseEntity> implements InMemoryStorage<T> {
    protected final ConcurrentHashMap<Long, T> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(idSeq.getAndIncrement());
        }

        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
