package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional(readOnly = true)
    public void authenticate(String username, String password) {
        validateCredentials(username, password);

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    private void validateCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
    }
}
