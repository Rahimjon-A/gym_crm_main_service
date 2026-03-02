package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.model.Trainer;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainerDaoImpl extends AbstractBaseDAO<Trainer> implements TrainerDAO {
    private static final String FIND_BY_USERNAME_QUERY = "SELECT t FROM Trainer t WHERE t.username = :username";
    private static final String GET_UNASSIGNED_TRAINERS_QUERY =
            "SELECT tr FROM Trainer tr " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT 1 FROM Training t " +
                    "    WHERE t.trainer = tr " +
                    "    AND t.trainee.username = :username" +
                    ")";
    private static final String PARAM_USERNAME = "username";

    public TrainerDaoImpl() {
        super(Trainer.class);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        log.debug("Finding Trainer by username: {}", username);
        return getEntityManager().createQuery(FIND_BY_USERNAME_QUERY, Trainer.class)
                .setParameter(PARAM_USERNAME, username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.debug("Fetching unassigned trainers for trainee: {}", traineeUsername);
        return getEntityManager().createQuery(GET_UNASSIGNED_TRAINERS_QUERY, Trainer.class)
                .setParameter(PARAM_USERNAME, traineeUsername)
                .getResultList();
    }
}
