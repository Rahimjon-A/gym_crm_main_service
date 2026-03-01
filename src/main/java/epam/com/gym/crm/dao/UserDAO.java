package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.User;
import java.util.Optional;

public interface UserDAO extends BaseDAO<User> {
    Optional<User> findByUsername(String username);
}
