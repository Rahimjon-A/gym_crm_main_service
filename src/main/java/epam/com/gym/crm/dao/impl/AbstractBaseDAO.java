package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.BaseDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractBaseDAO<T> implements BaseDAO<T> {
    private static final String FROM_CLAUSE = "FROM ";

    @Getter
    private EntityManager entityManager;
    private final Class<T> entityClass;

    protected AbstractBaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T create(T entity) {
        log.debug("Saving new {} to database", entityClass.getSimpleName());
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        log.debug("Updating {} in database", entityClass.getSimpleName());
        return entityManager.merge(entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        log.debug("Fetching {} with ID: {}", entityClass.getSimpleName(), id);
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        log.debug("Fetching all {} records", entityClass.getSimpleName());
        return entityManager.createQuery(FROM_CLAUSE + entityClass.getName(), entityClass)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting {} with ID: {}", entityClass.getSimpleName(), id);
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
