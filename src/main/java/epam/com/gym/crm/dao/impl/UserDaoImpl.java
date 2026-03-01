package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractBaseDAO<User> implements UserDAO {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return getEntityManager().createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }
}