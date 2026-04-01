package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.service.JwtService;
import epam.com.gym.crm.service.TokenBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final Map<String, LocalDateTime> blacklistedTokens = new ConcurrentHashMap<>();
    private JwtService jwtService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void blacklist(String token) {
        String username = jwtService.extractUsername(token);
        blacklistedTokens.put(token, LocalDateTime.now());
        log.info("Token blacklisted for user: {}", username);

        cleanExpiredTokens();
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    private void cleanExpiredTokens() {
        blacklistedTokens.entrySet().removeIf(entry -> {
            try {
                return entry.getValue().isBefore(LocalDateTime.now().minusHours(24));
            } catch (Exception e) {
                return true;
            }
        });
        log.debug("Cleaned up expired tokens from blacklist");
    }
}
