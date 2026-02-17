package epam.com.gym.crm.storage;

import java.util.List;
import java.util.Optional;

public interface InMemoryStorage<T> {

    T save(T entity);

    T update(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void delete(Long id);
}

