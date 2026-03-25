package epam.com.gym.crm.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadHealthIndicatorTest {

    private static final String KEY_ACTIVE_THREADS = "activeThreadCount";
    private static final String KEY_PEAK_THREADS = "peakThreadCount";
    private static final String KEY_DAEMON_THREADS = "daemonThreadCount";
    private static final String KEY_DEADLOCKED_THREADS = "deadlockedThreadCount";
    private static final String KEY_MESSAGE = "message";

    private final ThreadHealthIndicator healthIndicator = new ThreadHealthIndicator();

    @Test
    void health_shouldReturnHealthStatusWithThreadDetails() {
        Health result = healthIndicator.health();

        assertNotNull(result);
        
        assertEquals(Status.UP, result.getStatus());

        assertTrue(result.getDetails().containsKey(KEY_ACTIVE_THREADS));
        assertTrue(result.getDetails().containsKey(KEY_PEAK_THREADS));
        assertTrue(result.getDetails().containsKey(KEY_DAEMON_THREADS));
        assertTrue(result.getDetails().containsKey(KEY_DEADLOCKED_THREADS));
        assertTrue(result.getDetails().containsKey(KEY_MESSAGE));
        
        assertEquals(0, result.getDetails().get(KEY_DEADLOCKED_THREADS));
    }
}
