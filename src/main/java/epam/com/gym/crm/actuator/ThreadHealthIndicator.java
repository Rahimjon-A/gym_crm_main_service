package epam.com.gym.crm.actuator;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

@Component
public class ThreadHealthIndicator implements HealthIndicator {

    private static final String KEY_ACTIVE_THREADS = "activeThreadCount";
    private static final String KEY_PEAK_THREADS = "peakThreadCount";
    private static final String KEY_DAEMON_THREADS = "daemonThreadCount";
    private static final String KEY_DEADLOCKED_THREADS = "deadlockedThreadCount";
    private static final String KEY_MESSAGE = "message";

    private static final String MSG_THREADS_OK = "Thread activity is normal. No deadlocks detected.";
    private static final String MSG_DEADLOCK_DETECTED = "CRITICAL: Deadlocked threads detected!";

    @Override
    public Health health() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        int activeThreads = threadBean.getThreadCount();
        int peakThreads = threadBean.getPeakThreadCount();
        int daemonThreads = threadBean.getDaemonThreadCount();
        
        long[] deadlockedThreads = threadBean.findDeadlockedThreads();
        int deadlockedCount = (deadlockedThreads != null) ? deadlockedThreads.length : 0;

        Health.Builder builder;

        if (deadlockedCount > 0) {
            builder = Health.down();
        } else {
            builder = Health.up();
        }

        return builder
                .withDetail(KEY_ACTIVE_THREADS, activeThreads)
                .withDetail(KEY_PEAK_THREADS, peakThreads)
                .withDetail(KEY_DAEMON_THREADS, daemonThreads)
                .withDetail(KEY_DEADLOCKED_THREADS, deadlockedCount)
                .withDetail(KEY_MESSAGE, deadlockedCount > 0 ? MSG_DEADLOCK_DETECTED : MSG_THREADS_OK)
                .build();
    }
}
