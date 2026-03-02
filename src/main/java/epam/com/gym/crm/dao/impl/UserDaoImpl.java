package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractBaseDAO<User> implements UserDAO {
    private static final String FIND_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = :username";
    private static final String PARAM_USERNAME = "username";

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return getEntityManager().createQuery(FIND_BY_USERNAME_QUERY, User.class)
                .setParameter(PARAM_USERNAME, username)
                .getResultStream()
                .findFirst();
    }
}
