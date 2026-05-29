package epam.com.gym.crm.component.steps;

import epam.com.gym.crm.component.context.SharedTestContext;
import epam.com.gym.crm.component.helper.RestHelper;
import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.model.common.Credentials;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class TrainerSteps {

    private static final String TRAINER_URL = "/api/v1/trainers";
    private static final String AUTH_URL = "/api/v1/auth";
    private static final String TOKEN_FIELD = "token";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    private RestHelper restHelper;

    @Autowired
    private SharedTestContext sharedContext;

    @Autowired
    private Environment environment;

    @Before
    public void setUp() {
        restHelper = new RestHelper(environment);
    }

    @Given("a registered trainer with firstName {string} and lastName {string} and specializationId {long}")
    public void registerTrainer(String firstName, String lastName, long specId) {
        TrainerCreateRequest request = new TrainerCreateRequest(
                firstName,
                lastName,
                true,
                specId
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Credentials> response = restHelper
                .post(TRAINER_URL, request, Credentials.class);
        assertNotNull(response.getBody());
        sharedContext.setTrainerCredentials(response.getBody());
        sharedContext.setLastResponse(response);

    }

    @When("a new trainer registers with firstName {string} and lastName {string} and specializationId {long}")
    public void registerNewTrainer(String firstName, String lastName, long specId) {
        TrainerCreateRequest request = new TrainerCreateRequest(
                firstName, lastName, true, specId);

        ResponseEntity<Credentials> response = restHelper
                .post(TRAINER_URL, request, Credentials.class);
        sharedContext.setLastResponse(response);
        if (response.getBody() != null) {
            sharedContext.setTrainerCredentials(response.getBody());
        }
    }

    @Given("the trainer is authenticated")
    public void authenticateTrainer() {
        Credentials credentials = sharedContext.getTrainerCredentials();
        String token = login(credentials.getUsername(), credentials.getPassword());
        sharedContext.setJwtToken(token);
    }

    @When("the client requests the trainer profile")
    public void getTrainerProfile() {
        String username = sharedContext.getTrainerCredentials().getUsername();
        ResponseEntity<Map> response = restHelper
                .get(TRAINER_URL + "/" + username, sharedContext.getJwtToken(), Map.class);
        sharedContext.setLastResponse(response);
    }

    @When("the client updates the trainer firstName to {string} and lastName to {string}")
    public void updateTrainer(String firstName, String lastName) {
        String username = sharedContext.getTrainerCredentials().getUsername();
        TrainerUpdateRequest body = new TrainerUpdateRequest();
        body.setUsername(username);
        body.setFirstName(firstName);
        body.setLastName(lastName);
        body.setIsActive(true);
        body.setSpecializationId(1L);

        ResponseEntity<Map> response = restHelper
                .put(TRAINER_URL + "/" + username, body, sharedContext.getJwtToken(), Map.class);
        sharedContext.setLastResponse(response);
    }

    private String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of(
                USERNAME_FIELD, username,
                PASSWORD_FIELD, password);
        ResponseEntity<Map> response = restHelper.execute(
                AUTH_URL, HttpMethod.POST,
                new HttpEntity<>(body, headers), Map.class);
        assertNotNull(response.getBody());
        return response.getBody().get(TOKEN_FIELD).toString();
    }
}
