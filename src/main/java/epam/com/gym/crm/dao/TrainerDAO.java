package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.Trainer;
import java.util.Optional;

public interface TrainerDAO extends BaseDAO<Trainer> {
    Optional<Trainer> findByUsername(String username);
}
