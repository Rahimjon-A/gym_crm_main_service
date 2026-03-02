package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.model.Trainee;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Repository
public class TraineeDaoImpl extends AbstractBaseDAO<Trainee> implements TraineeDAO {
    private static final String FIND_BY_USERNAME_QUERY = "SELECT t FROM Trainee t WHERE t.username = :username";
    private static final String PARAM_USERNAME = "username";

    public TraineeDaoImpl() {
        super(Trainee.class);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.debug("Finding Trainee by username: {}", username);
        return getEntityManager().createQuery(FIND_BY_USERNAME_QUERY, Trainee.class)
                .setParameter(PARAM_USERNAME, username)
                .getResultStream()
                .findFirst();
    }
}
