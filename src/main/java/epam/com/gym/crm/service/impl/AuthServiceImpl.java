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

        if (!user.getIsActive()) {
            throw new IllegalStateException("User profile is deactivated");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        validateCredentials(username, oldPassword);

        if (newPassword == null || newPassword.isBlank() || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters and not blank");
        }

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Password is incorrect");
        }

        user.setPassword(newPassword);
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void activateUser(String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getIsActive()) {
            throw new IllegalStateException("User already active");
        }

        user.setIsActive(true);
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void deactivateUser(String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getIsActive()) {
            throw new IllegalStateException("User already inactive");
        }

        user.setIsActive(false);
        userDAO.update(user);
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
