package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDAO {
    private static final String FIND_BY_NAME_QUERY = "SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :name";
    private static final String FIND_ALL_QUERY = "FROM TrainingType";
    private static final String PARAM_NAME = "name";

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        log.debug("Finding TrainingType by id: {}", id);
        return Optional.ofNullable(entityManager.find(TrainingType.class, id));
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        if (name == null) return Optional.empty();
        log.debug("Finding TrainingType by name: {}", name);
        return entityManager.createQuery(FIND_BY_NAME_QUERY, TrainingType.class)
                .setParameter(PARAM_NAME, name.toLowerCase())
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<TrainingType> findAll() {
        log.debug("Fetching all {} records", TrainingType.class.getName());
        return entityManager.createQuery(FIND_ALL_QUERY, TrainingType.class)
                .getResultList();
    }
}
