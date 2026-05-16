package epam.com.gym.crm.component.steps;

import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeUpdateRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class TraineeSteps {

    private static final String TRAINEE_URL = "/api/v1/trainees";
    private static final String AUTH_URL = "/api/v1/auth";
    private static final String PORT_PROPERTY = "local.server.port";
    private static final String TOKEN_FIELD = "token";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String FIRST_NAME_KEY = "firstName";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SharedTestContext sharedContext;

    @Autowired
    private Environment environment;

    @When("a new trainee registers with firstName {string} and lastName {string}")
    public void registerNewTrainee(String firstName, String lastName) {
        TraineeCreateRequest request = new TraineeCreateRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setIsActive(true);

        try {
            ResponseEntity<Credentials> response = restTemplate.postForEntity(
                    baseUrl(TRAINEE_URL), request, Credentials.class);
            sharedContext.setLastResponse(response);
            if (response.getBody() != null) {
                sharedContext.setCredentials(response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @And("the response should contain a username and password")
    public void verifyUsernameAndPassword() {
        Credentials creds = (Credentials) sharedContext.getLastResponse().getBody();
        assertNotNull(creds, "Response body should contain credentials");
        assertNotNull(creds.getUsername(), "Username should not be null");
        assertNotNull(creds.getPassword(), "Password should not be null");
    }

    @Given("the user is authenticated")
    public void authenticateUser() {
        Credentials credentials = sharedContext.getCredentials();
        String token = login(credentials.getUsername(), credentials.getPassword());
        sharedContext.setJwtToken(token);
    }

    @When("the client requests the trainee profile")
    public void getTraineeProfile() {
        String username = sharedContext.getCredentials().getUsername();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl(TRAINEE_URL + "/" + username),
                    HttpMethod.GET, new HttpEntity<>(authHeaders()), Map.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("the client requests profile for username {string}")
    public void getProfileByUsername(String username) {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl(TRAINEE_URL + "/" + username),
                    HttpMethod.GET, new HttpEntity<>(authHeaders()), Map.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("the client requests profile for username {string} without auth")
    public void getProfileWithoutAuth(String username) {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl(TRAINEE_URL + "/" + username),
                    HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Map.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("the client updates the trainee firstName to {string} and lastName to {string}")
    public void updateTrainee(String firstName, String lastName) {
        String username = sharedContext.getCredentials().getUsername();
        TraineeUpdateRequest body = new TraineeUpdateRequest(
                firstName,
                lastName,
                true,
                null,
                "Tashkent",
                username);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl(TRAINEE_URL + "/" + username),
                    HttpMethod.PUT, new HttpEntity<>(body, authHeaders()), Map.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("the client deletes the trainee profile")
    public void deleteTrainee() {
        String username = sharedContext.getCredentials().getUsername();
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    baseUrl(TRAINEE_URL + "/" + username),
                    HttpMethod.DELETE, new HttpEntity<>(authHeaders()), Void.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @And("the response should contain firstName {string}")
    public void verifyFirstName(String expectedFirstName) {
        Map<?, ?> body = (Map<?, ?>) sharedContext.getLastResponse().getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals(expectedFirstName, body.get(FIRST_NAME_KEY));
    }

    private String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of(USERNAME_FIELD, username, PASSWORD_FIELD, password);
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl(AUTH_URL), HttpMethod.POST,
                new HttpEntity<>(body, headers), Map.class);
        assertNotNull(response.getBody(), "Login response should not be null");
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
