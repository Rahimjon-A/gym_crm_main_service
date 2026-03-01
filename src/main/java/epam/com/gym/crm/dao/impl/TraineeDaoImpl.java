package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TraineeDaoImpl extends AbstractBaseDAO<Trainee> implements TraineeDAO {

    public TraineeDaoImpl() {
        super(Trainee.class);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.info("Finding Trainee by username: {}", username);
        return getEntityManager().createQuery("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username", Trainee.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        String hql = "SELECT tr FROM Trainer tr " +
                "WHERE tr.user.isActive = true AND tr.id NOT IN " +
                "(SELECT t.trainer.id FROM Training t WHERE t.trainee.user.username = :username)";

        return getEntityManager().createQuery(hql, Trainer.class)
                .setParameter("username", traineeUsername)
                .getResultList();
    }
}
