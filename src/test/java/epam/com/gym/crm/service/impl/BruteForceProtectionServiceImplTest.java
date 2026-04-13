package epam.com.gym.crm.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BruteForceProtectionServiceImplTest {

    private static final String USERNAME = "john.doe";

    private BruteForceProtectionServiceImpl bruteForceProtectionService;

    @BeforeEach
    void setUp() {
        bruteForceProtectionService = new BruteForceProtectionServiceImpl();
        bruteForceProtectionService.init();
    }

    @Test
    void isBlocked_shouldReturnFalse_whenUserHasNoFailedAttempts() {
        assertFalse(bruteForceProtectionService.isBlocked(USERNAME));
    }

    @Test
    void loginFailed_shouldNotBlockUser_beforeMaxAttempts() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

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
    void loginSucceeded_shouldClearBlock() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        bruteForceProtectionService.loginSucceeded(USERNAME);

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME));
    }

    @Test
    void isBlocked_shouldReturnFalse_afterTtlExpires() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        BruteForceProtectionServiceImpl shortTtlService =
                new BruteForceProtectionServiceImpl();

        Cache<String, Integer> shortCache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MILLISECONDS)
                .build();

        Field cacheField = BruteForceProtectionServiceImpl.class
                .getDeclaredField("attemptCache");
        cacheField.setAccessible(true);
        cacheField.set(shortTtlService, shortCache);

        shortTtlService.loginFailed(USERNAME);
        shortTtlService.loginFailed(USERNAME);
        shortTtlService.loginFailed(USERNAME);

        assertTrue(shortTtlService.isBlocked(USERNAME));

        Thread.sleep(200);

        assertFalse(shortTtlService.isBlocked(USERNAME));
    }
}
