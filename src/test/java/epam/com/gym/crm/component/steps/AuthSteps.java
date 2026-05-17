package epam.com.gym.crm.component.steps;

import epam.com.gym.crm.component.context.SharedTestContext;
import epam.com.gym.crm.component.helper.RestHelper;
import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.model.common.Credentials;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthSteps {

    private static final String TRAINEE_URL = "/api/v1/trainees";
    private static final String AUTH_URL = "/api/v1/auth";
    private static final String WRONG_PASSWORD = "wrongpassword";
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

    @Given("a registered trainee with firstName {string} and lastName {string}")
    public void registerTrainee(String firstName, String lastName) {
        TraineeCreateRequest request = new TraineeCreateRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setIsActive(true);

//        try {
//            ResponseEntity<Credentials> response = restTemplate.postForEntity(
//                    baseUrl(TRAINEE_URL), request, Credentials.class);
//            assertNotNull(response.getBody(), "Registration should return credentials");
//            sharedContext.setCredentials(response.getBody());
//            sharedContext.setLastResponse(response);
//        } catch (HttpClientErrorException e) {
//            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
//        }
        ResponseEntity<Credentials> response = restHelper.post(TRAINEE_URL, request, Credentials.class);
        assertNotNull(response.getBody(), "Registration should return credentials");
        sharedContext.setCredentials(response.getBody());
        sharedContext.setLastResponse(response);

    }

    @When("the user logs in with correct credentials")
    public void loginWithCorrectCredentials() {
        Credentials credentials = sharedContext.getCredentials();
        ResponseEntity<Map> response = postLogin(credentials.getUsername(), credentials.getPassword());
        sharedContext.setLastResponse(response);

        if (response.getBody() != null && response.getBody().containsKey(TOKEN_FIELD)) {
            sharedContext.setJwtToken(response.getBody().get(TOKEN_FIELD).toString());
        }
    }

    @When("the user logs in with wrong password {string}")
    public void loginWithWrongPassword(String wrongPassword) {
        Credentials credentials = sharedContext.getCredentials();
        sharedContext.setLastResponse(postLogin(credentials.getUsername(), wrongPassword));
    }

    @When("the user fails to login 3 times with wrong password")
    public void failLoginThreeTimes() {
        Credentials credentials = sharedContext.getCredentials();
        ResponseEntity<Map> last = null;
        for (int i = 0; i < 3; i++) {
            last = postLogin(credentials.getUsername(), WRONG_PASSWORD);
        }
        sharedContext.setLastResponse(last);
    }

    @Then("the response status should be {int}")
    public void verifyResponseStatus(int expectedStatus) {
        assertEquals(expectedStatus, sharedContext.getLastResponse().getStatusCode().value());
    }

    @And("the response should contain a JWT token")
    public void verifyJwtTokenPresent() {
        Map<?, ?> body = (Map<?, ?>) sharedContext.getLastResponse().getBody();
        assertNotNull(body, "Response body should not be null");
        assertNotNull(body.get(TOKEN_FIELD), "JWT token should be present");
    }

    @And("the user should be temporarily blocked")
    public void verifyUserIsBlocked() {
        Credentials credentials = sharedContext.getCredentials();
        ResponseEntity<Map> response = postLogin(credentials.getUsername(), credentials.getPassword());
        assertEquals(401, response.getStatusCode().value(),
                "Blocked user should get 401 even with correct password");
    }

    private ResponseEntity<Map> postLogin(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of(
                USERNAME_FIELD, username,
                PASSWORD_FIELD, password);
        return restHelper.execute(
                AUTH_URL, HttpMethod.POST,
                new HttpEntity<>(body, headers), Map.class);
    }

}
