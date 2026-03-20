package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.service.UserService;
import epam.com.gym.crm.utility.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String SEPARATOR = ".";

    @Autowired
    private UserDAO<User> userDAO;

    @Override
    public String generatePassword() {
        log.debug("Generating random password");
        String password = PasswordGenerator.generate();
        log.debug("Password generated successfully");
        return password;
    }

    @Override
    public String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            log.error("Attempt to generate username with invalid input: firstName='{}', lastName='{}'", firstName, lastName);
            throw new ValidationException("First name and last name must not be null or blank");
        }

        String base = firstName.trim().toLowerCase() + SEPARATOR + lastName.trim().toLowerCase();
        log.debug("Generating username based on base='{}'", base);

        String candidate = base;
        int counter = 1;

        while(userDAO.findByUsername(candidate).isPresent()) {
            candidate = base + counter;
            counter++;
        }

        log.debug("Final username generated='{}'", candidate);
        return candidate;
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        validateCredentials(username, oldPassword, newPassword);
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new AuthenticationException("Password is incorrect");
        }

        if (newPassword.length() < 10) {
            throw new ValidationException("New Password is too short, at least 10 characters");
        }

        user.setPassword(newPassword);
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void activateUser(String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActive(true);
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void deactivateUser(String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActive(false);
        userDAO.update(user);
    }

    private void validateCredentials(String username, String oldPassword, String newPassword) {
        if (username == null || username.isBlank() || oldPassword == null || oldPassword.isBlank()) {
            throw new ValidationException("Username and old password must not be blank");
        }
        if (newPassword == null || newPassword.isBlank() || newPassword.length() < 10) {
            throw new ValidationException("New password must be at least 10 characters and not blank");
        }
    }
}
