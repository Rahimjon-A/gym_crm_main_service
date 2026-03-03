package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.User;
import java.util.Optional;

public interface UserDAO<T extends User> extends BaseDAO<T> {
    Optional<T> findByUsername(String username);
}
