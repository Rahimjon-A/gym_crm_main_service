package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.service.BruteForceProtectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MINUTES = 5;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> blockCache = new ConcurrentHashMap<>();

    @Override
    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);
        log.warn("Failed login attempt #{} for user: {}", attempts, username);

        if (attempts >= MAX_ATTEMPTS) {
            blockCache.put(username, LocalDateTime.now());
            log.warn("User BLOCKED after {} failed attempts: {}", MAX_ATTEMPTS, username);
        }
    }

    @Override
    public void loginSucceeded(String username) {
        log.debug("Login succeeded, clearing attempt cache for user: {}", username);
        attemptsCache.remove(username);
        blockCache.remove(username);
    }

    @Override
    public boolean isBlocked(String username) {
        if (!blockCache.containsKey(username)) {
            return false;
        }

        LocalDateTime blockedAt = blockCache.get(username);
        boolean stillBlocked = LocalDateTime.now()
                .isBefore(blockedAt.plusMinutes(BLOCK_DURATION_MINUTES));

        if (!stillBlocked) {
            log.info("Block expired for user: {}, clearing cache", username);
            attemptsCache.remove(username);
            blockCache.remove(username);
        }

        return stillBlocked;
    }
}
