package epam.com.gym.crm.model.common;

import epam.com.gym.crm.exception.AuthenticationException;

public record Credentials(String username, String password) {
    public Credentials {
        if (username == null || username.isBlank()) {
            throw new AuthenticationException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new AuthenticationException("Password cannot be empty");
        }
    }
}
