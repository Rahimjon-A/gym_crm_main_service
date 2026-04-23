package epam.com.gym.crm.config;

import org.springframework.cloud.openfeign.CircuitBreakerNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignCircuitBreakerConfig {
    private static final String CIRCUIT_BREAKER_NAME = "gym-trainer-workload";

    @Bean
    public CircuitBreakerNameResolver circuitBreakerNameResolver() {
        return (feignClientName, target, method) -> feignClientName;
    }
}
