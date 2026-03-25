package epam.com.gym.crm.actuator;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MemoryHealthIndicator implements HealthIndicator {

    private static final String KEY_FREE_MEMORY = "freeMemory";
    private static final String KEY_USED_MEMORY = "usedMemory";
    private static final String KEY_TOTAL_MEMORY = "totalMemory";
    private static final String KEY_MAX_MEMORY = "maxMemory";
    private static final String KEY_PROCESSORS_MEMORY = "availableProcessors";
    private static final String KEY_MESSAGE = "message";
    private static final String USED_SPACE_PERCENT = "usedMemoryPercentage";
    private static final String MB = " MB";
    private static final String PERCENT = "%";

    private static final long BYTES_IN_MB = 1024 * 1024;
    
    private static final String MSG_MEMORY_OK = "Memory levels are sufficient.";
    private static final String MSG_MEMORY_LOW = "Memory is running dangerously low!";
    
    private static final long THRESHOLD_LOW_MEMORY = 10 * 1024 * 1024;

    @Override
    public Health health() {
        Runtime runtime = Runtime.getRuntime();

        long freeMemoryBytes = runtime.freeMemory();
        long totalMemoryBytes = runtime.totalMemory();
        long maxMemoryBytes = runtime.maxMemory();
        long usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;

        long freeMemoryMb = freeMemoryBytes / BYTES_IN_MB;
        long totalMemoryMb = totalMemoryBytes / BYTES_IN_MB;
        long maxMemoryMb = maxMemoryBytes / BYTES_IN_MB;
        long usedMemoryMb = usedMemoryBytes / BYTES_IN_MB;

        long usedMemoryPercent = (usedMemoryBytes * 100) / maxMemoryBytes;

        Health.Builder builder;

        if (freeMemoryBytes < THRESHOLD_LOW_MEMORY) {
            builder = Health.down();
        } else {
            builder = Health.up();
        }

        return builder
                .withDetail(KEY_FREE_MEMORY, freeMemoryMb + MB)
                .withDetail(KEY_USED_MEMORY, usedMemoryMb + MB)
                .withDetail(KEY_TOTAL_MEMORY, totalMemoryMb + MB)
                .withDetail(KEY_MAX_MEMORY, maxMemoryMb + MB)
                .withDetail(USED_SPACE_PERCENT, usedMemoryPercent + PERCENT)
                .withDetail(KEY_PROCESSORS_MEMORY, runtime.availableProcessors())
                .withDetail(KEY_MESSAGE,
                        freeMemoryBytes < THRESHOLD_LOW_MEMORY ? MSG_MEMORY_LOW : MSG_MEMORY_OK)
                .build();
    }
}