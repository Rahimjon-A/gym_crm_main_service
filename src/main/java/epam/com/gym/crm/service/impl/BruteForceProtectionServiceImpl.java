package epam.com.gym.crm.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import epam.com.gym.crm.service.BruteForceProtectionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MINUTES = 5;
    private Cache<String, Integer> attemptCache;

    @PostConstruct
    public void init() {
        this.attemptCache = Caffeine.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_MINUTES, TimeUnit.MINUTES)
                .build();
        log.info("Brute force cache initialized with TTL: {} minutes", BLOCK_DURATION_MINUTES);
    }

    @Override
    public void loginFailed(String username) {
        int attempts = attemptCache.asMap()
                .merge(username, 1, Integer::sum);
        log.warn("Failed login attempt #{} for user: {}", attempts, username);

        if (attempts >= MAX_ATTEMPTS) {
            log.error("User BLOCKED after {} failed attempts: {}", MAX_ATTEMPTS, username);
        }
    }

    @Override
    public void loginSucceeded(String username) {
        attemptCache.invalidate(username);
        log.debug("Login succeeded, clearing attempt cache for user: {}", username);
    }

    @Override
    public boolean isBlocked(String username) {
        Integer attempts = attemptCache.getIfPresent(username);
        boolean blocked = attempts != null && attempts >= MAX_ATTEMPTS;
        if (blocked) {
            log.warn("Access denied — user is blocked: {}", username);
        }
        return blocked;
    }
}
