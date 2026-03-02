package epam.com.gym.crm.model.common;

public record Credentials(String username, String password) {
    public Credentials {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}
