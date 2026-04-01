package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import epam.com.gym.crm.service.BruteForceProtectionService;
import epam.com.gym.crm.service.JwtService;
import epam.com.gym.crm.service.TokenBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private UserDAO<User> userDAO;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private BruteForceProtectionService bruteForceProtectionService;
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    public void setUserDAO(UserDAO<User> userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setBruteForceProtectionService(BruteForceProtectionService bruteForceProtectionService) {
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Autowired
    public void setTokenBlacklistService(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    @Transactional(readOnly = true)
    public String authenticate(Credentials credentials) {
        validate(credentials);
        String username = credentials.getUsername();
        log.info("Authentication attempt for user: {}", username);

        User user = userDAO.findByUsername(credentials.getUsername())
                .orElseThrow(() -> {
                    bruteForceProtectionService.loginFailed(username);
                    log.warn("Authentication failed — user not found: {}", credentials.getUsername());
                    return new AuthenticationException("Invalid username or password");
                });

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            bruteForceProtectionService.loginFailed(username);
            log.warn("Authentication failed — wrong password for user: {}", credentials.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        bruteForceProtectionService.loginSucceeded(username);
        String token = jwtService.generateToken(user);
        log.info("Authentication successful, JWT issued for user: {}", credentials.getUsername());
        return token;
    }

    @Override
    public void logout(String token) {
        String username = jwtService.extractUsername(token);
        log.info("Logging out user: {}", username);
        tokenBlacklistService.blacklist(token);
    }

    private void validate(Credentials credentials) {
        String username = credentials.getUsername();
        if (username == null || username.isBlank()) {
            throw new AuthenticationException("Username cannot be empty");
        }
        if (credentials.getPassword() == null || credentials.getPassword().isBlank()) {
            throw new AuthenticationException("Password cannot be empty");
        }

        if (bruteForceProtectionService.isBlocked(username)) {
            log.warn("Authentication blocked for user: {} — too many failed attempts", username);
            throw new AuthenticationException(
                    "Account temporarily blocked. Please try again in 5 minutes.");
        }
    }
}
