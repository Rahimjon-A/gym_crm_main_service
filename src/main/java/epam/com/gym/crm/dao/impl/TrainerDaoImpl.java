package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.model.Trainer;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainerDaoImpl extends UserDaoImpl<Trainer> implements TrainerDAO {
    private static final String GET_UNASSIGNED_TRAINERS_QUERY = """
            SELECT tr FROM Trainer tr WHERE NOT EXISTS
                (SELECT 1 FROM Training t WHERE
                t.trainer = tr AND t.trainee.username = :username)
            """;
    private static final String PARAM_USERNAME = "username";

    public TrainerDaoImpl() {
        super(Trainer.class);
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.debug("Fetching unassigned trainers for trainee: {}", traineeUsername);
        return getEntityManager().createQuery(GET_UNASSIGNED_TRAINERS_QUERY, Trainer.class)
                .setParameter(PARAM_USERNAME, traineeUsername)
                .getResultList();
    }
}
