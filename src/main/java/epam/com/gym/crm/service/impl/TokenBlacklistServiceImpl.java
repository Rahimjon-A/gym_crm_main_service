package epam.com.gym.crm.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import epam.com.gym.crm.service.JwtService;
import epam.com.gym.crm.service.TokenBlacklistService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private Cache<String, Boolean> blacklistedTokens;
    private JwtService jwtService;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @PostConstruct
    public void init() {
        this.blacklistedTokens = Caffeine.newBuilder()
                .expireAfterWrite(jwtExpirationMs, TimeUnit.MILLISECONDS)
                .build();
        log.info("Token blacklist cache initialized with TTL: {}ms via @PostConstruct", jwtExpirationMs);
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void blacklist(String token) {
        String username = jwtService.extractUsername(token);
        blacklistedTokens.put(token, Boolean.TRUE);
        log.info("Token blacklisted for user: {}", username);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklistedTokens != null && blacklistedTokens.getIfPresent(token) != null;
    }
}
