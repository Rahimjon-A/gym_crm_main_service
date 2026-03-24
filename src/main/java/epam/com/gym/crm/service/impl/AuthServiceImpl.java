package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.ValidationException;
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
        validate(credentials);
        User user = userDAO.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!credentials.getPassword().equals(user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    private void validate(Credentials credentials) {
        if (credentials.getUsername() == null || credentials.getUsername().isBlank()) {
            throw new AuthenticationException("Username cannot be empty");
        }
        if (credentials.getPassword() == null || credentials.getPassword().isBlank()) {
            throw new AuthenticationException("Password cannot be empty");
        }
    }
}
