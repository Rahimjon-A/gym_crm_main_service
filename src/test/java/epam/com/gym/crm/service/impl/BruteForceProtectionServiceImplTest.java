package epam.com.gym.crm.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BruteForceProtectionServiceImplTest {

    private static final String USERNAME = "john.doe";

    private BruteForceProtectionServiceImpl bruteForceProtectionService;

    @BeforeEach
    void setUp() {
        bruteForceProtectionService = new BruteForceProtectionServiceImpl();
    }

    @Test
    void isBlocked_shouldReturnFalse_whenUserHasNoFailedAttempts() {
        assertFalse(bruteForceProtectionService.isBlocked(USERNAME));
    }

    @Test
    void loginFailed_shouldBlockUser_afterMaxAttempts() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        assertTrue(bruteForceProtectionService.isBlocked(USERNAME));
    }

    @Test
    void loginFailed_shouldNotBlockUser_beforeMaxAttempts() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME));
    }

    @Test
    void loginSucceeded_shouldClearBlock_afterPreviousFailures() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        bruteForceProtectionService.loginSucceeded(USERNAME);

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME));
    }

    @Test
    void isBlocked_shouldReturnFalse_whenBlockExpired() throws Exception {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        Field blockCacheField = BruteForceProtectionServiceImpl.class
                .getDeclaredField("blockCache");
        blockCacheField.setAccessible(true);

        Map<String, LocalDateTime> blockCache =
                (Map<String, LocalDateTime>) blockCacheField.get(bruteForceProtectionService);

        blockCache.put(USERNAME, LocalDateTime.now().minusMinutes(10));

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME));
    }
}
