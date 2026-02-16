package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface InMemoryStorage<T extends BaseEntity> {

    T save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void delete(Long id);
}
