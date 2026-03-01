package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.model.Trainer;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Repository
public class TrainerDaoImpl extends AbstractBaseDAO<Trainer> implements TrainerDAO {

    public TrainerDaoImpl() {
        super(Trainer.class);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        log.info("Finding Trainer by username: {}", username);
        return getEntityManager().createQuery("SELECT t FROM Trainer t JOIN t.user u WHERE u.username = :username", Trainer.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }
}