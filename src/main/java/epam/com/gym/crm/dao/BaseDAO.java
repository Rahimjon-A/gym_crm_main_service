package epam.com.gym.crm.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> {
    T create(T entity);
    T update(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void delete(Long id);
}
