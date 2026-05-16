package epam.com.gym.crm.component.steps;
 
import epam.com.gym.crm.model.common.Credentials;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
 
@Getter
@Setter
@Component
@ScenarioScope
public class SharedTestContext {

    private Credentials credentials;
    private Credentials trainerCredentials;
    private String jwtToken;
    private ResponseEntity<?> lastResponse;
}
