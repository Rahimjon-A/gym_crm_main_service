package epam.com.gym.crm.component.steps;

import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.model.common.Credentials;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class TrainerSteps {

    private static final String TRAINER_URL = "/api/v1/trainers";
    private static final String AUTH_URL = "/api/v1/auth";
    private static final String PORT_PROPERTY = "local.server.port";
    private static final String TOKEN_FIELD = "token";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SharedTestContext sharedContext;

    @Autowired
    private Environment environment;

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

        try {
            ResponseEntity<Credentials> response = restTemplate.exchange(
                    baseUrl(TRAINER_URL), HttpMethod.POST,
                    new HttpEntity<>(request, headers), Credentials.class);
            assertNotNull(response.getBody());
            sharedContext.setTrainerCredentials(response.getBody());
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("a new trainer registers with firstName {string} and lastName {string} and specializationId {long}")
    public void registerNewTrainer(String firstName, String lastName, long specId) {
        TrainerCreateRequest request = new TrainerCreateRequest(
                firstName,
                lastName,
                true,
                specId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Credentials> response = restTemplate.exchange(
                    baseUrl(TRAINER_URL), HttpMethod.POST,
                    new HttpEntity<>(request, headers), Credentials.class);
            sharedContext.setLastResponse(response);
            if (response.getBody() != null) {
                sharedContext.setTrainerCredentials(response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
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
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl(TRAINER_URL + "/" + username),
                    HttpMethod.GET, new HttpEntity<>(authHeaders()), Map.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
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

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl(TRAINER_URL + "/" + username),
                    HttpMethod.PUT, new HttpEntity<>(body, authHeaders()), Map.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    private String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of(USERNAME_FIELD, username, PASSWORD_FIELD, password);
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl(AUTH_URL), HttpMethod.POST,
                new HttpEntity<>(body, headers), Map.class);
        assertNotNull(response.getBody());
        return response.getBody().get(TOKEN_FIELD).toString();
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(sharedContext.getJwtToken());
        return headers;
    }

    private String baseUrl(String path) {
        return "http://localhost:" + environment.getProperty(PORT_PROPERTY) + path;
    }
}
