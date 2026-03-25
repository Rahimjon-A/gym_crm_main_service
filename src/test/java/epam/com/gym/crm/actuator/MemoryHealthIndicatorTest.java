package epam.com.gym.crm.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryHealthIndicatorTest {
    private static final String KEY_FREE_MEMORY = "freeMemory";
    private static final String KEY_USED_MEMORY = "usedMemory";
    private static final String KEY_TOTAL_MEMORY = "totalMemory";
    private static final String KEY_MAX_MEMORY = "maxMemory";
    private static final String KEY_PROCESSORS_MEMORY = "availableProcessors";
    private static final String KEY_MESSAGE = "message";
    private static final String USED_SPACE_PERCENT = "usedMemoryPercentage";

    private final MemoryHealthIndicator healthIndicator = new MemoryHealthIndicator();

    @Test
    void health_shouldReturnHealthStatusWithMemoryDetails() {
        Health result = healthIndicator.health();

        assertNotNull(result);
        
        assertEquals(Status.UP, result.getStatus());

        assertTrue(result.getDetails().containsKey(KEY_FREE_MEMORY));
        assertTrue(result.getDetails().containsKey(KEY_USED_MEMORY));
        assertTrue(result.getDetails().containsKey(KEY_TOTAL_MEMORY));
        assertTrue(result.getDetails().containsKey(KEY_MAX_MEMORY));
        assertTrue(result.getDetails().containsKey(USED_SPACE_PERCENT));
        assertTrue(result.getDetails().containsKey(KEY_PROCESSORS_MEMORY));
        assertTrue(result.getDetails().containsKey(KEY_MESSAGE));
    }
}
