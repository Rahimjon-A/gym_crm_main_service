package epam.com.gym.crm.service;

public interface BruteForceProtectionService {
    void loginFailed(String username);
    void loginSucceeded(String username);
    boolean isBlocked(String username);
}
