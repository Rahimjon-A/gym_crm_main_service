package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenBlacklistServiceImplTest {

    private static final String TOKEN = "some.jwt.token";
    private static final String USERNAME = "john.doe";

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TokenBlacklistServiceImpl tokenBlacklistService;

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
    void blacklist_shouldCleanExpiredTokens() throws Exception {
        Field field = TokenBlacklistServiceImpl.class
                .getDeclaredField("blacklistedTokens");
        field.setAccessible(true);

        Map<String, LocalDateTime> cache = (Map<String, LocalDateTime>) field.get(tokenBlacklistService);

        cache.put("old.token", LocalDateTime.now().minusHours(25));

        when(jwtService.extractUsername(TOKEN)).thenReturn(USERNAME);
        tokenBlacklistService.blacklist(TOKEN);

        assertFalse(tokenBlacklistService.isBlacklisted("old.token"));
        assertTrue(tokenBlacklistService.isBlacklisted(TOKEN));
    }
}
