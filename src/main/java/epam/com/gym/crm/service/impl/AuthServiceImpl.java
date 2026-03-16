package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDAO<User> userDAO;

    @Override
    @Transactional(readOnly = true)
    public void authenticate(Credentials credentials) {
        validateCredentials(credentials);

        User user = userDAO.findByUsername(credentials.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!credentials.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is deactivated");
        }
    }

    private void validateCredentials(Credentials credentials) {
        if (credentials.username() == null || credentials.username().isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (credentials.password() == null || credentials.password().isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
    }
}
