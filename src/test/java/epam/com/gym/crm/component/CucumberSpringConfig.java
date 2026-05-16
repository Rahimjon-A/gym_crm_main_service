package epam.com.gym.crm.component;
 
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
 
@CucumberContextConfiguration
@SpringBootTest(
        classes = epam.com.gym.crm.GymCRMApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class CucumberSpringConfig {
}
