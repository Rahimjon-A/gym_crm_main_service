package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.EntityNotFoundException;
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

        User user = userDAO.findByUsername(credentials.username())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!credentials.password().equals(user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

    }
}
