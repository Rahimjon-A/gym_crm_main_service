package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class TokenBlacklistServiceImplTest {

    private static final String TOKEN = "some.jwt.token";
    private static final String USERNAME = "john.doe";
    private static final long EXPIRATION_MS = 3600000;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TokenBlacklistServiceImpl tokenBlacklistService;

    @BeforeEach
    void setUp() {
        setField(tokenBlacklistService, "jwtExpirationMs", EXPIRATION_MS);

        tokenBlacklistService.init();
    }

    @Test
    void isBlacklisted_shouldReturnFalse_whenTokenNotBlacklisted() {
        assertFalse(tokenBlacklistService.isBlacklisted(TOKEN));
    }

    @Test
    void blacklist_shouldBlacklistToken() {
        when(jwtService.extractUsername(TOKEN)).thenReturn(USERNAME);

        tokenBlacklistService.blacklist(TOKEN);

        assertTrue(tokenBlacklistService.isBlacklisted(TOKEN));
    }

    @Test
    void cache_shouldBeInitializedSuccessfully() {
        Object cache = ReflectionTestUtils.getField(tokenBlacklistService, "blacklistedTokens");
        assertNotNull(cache, "Cache should be initialized after init()");
    }

    @Test
    void blacklist_shouldStoreTokenInCache() {
        when(jwtService.extractUsername(TOKEN)).thenReturn(USERNAME);
        tokenBlacklistService.blacklist(TOKEN);

        assertTrue(tokenBlacklistService.isBlacklisted(TOKEN));
    }
}
