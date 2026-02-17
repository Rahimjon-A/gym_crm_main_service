package epam.com.gym.crm.dao;

import epam.com.gym.crm.model.User;

public interface UserDAO<T extends User> extends BaseDAO<T> {
    boolean existsByUsername(String username);
}
