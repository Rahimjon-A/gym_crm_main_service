package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl<T extends User> extends AbstractBaseDAO<T> implements UserDAO<T> {
    private static final String FIND_BY_USERNAME_QUERY_TEMPLATE = "SELECT e FROM %s e WHERE e.username = :username";
    private static final String PARAM_USERNAME = "username";

    public UserDaoImpl(Class<T> entityClass) {
        super(entityClass);
    }

    public UserDaoImpl() {
        super((Class<T>) User.class);
    }

    @Override
    public Optional<T> findByUsername(String username) {
        String query = String.format(FIND_BY_USERNAME_QUERY_TEMPLATE, getEntityClass().getSimpleName());

        List<T> resultList = getEntityManager().createQuery(query, getEntityClass())
                .setParameter(PARAM_USERNAME, username)
                .getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));

    }
}
