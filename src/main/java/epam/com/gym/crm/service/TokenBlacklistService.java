package epam.com.gym.crm.service;

public interface TokenBlacklistService {
    void blacklist(String token);
    boolean isBlacklisted(String token);
}
