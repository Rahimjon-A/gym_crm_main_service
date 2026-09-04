package epam.com.gym.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GymCRMApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(GymCRMApplication.class, args);
    }
}
