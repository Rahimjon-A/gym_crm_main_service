package epam.com.gym.crm.component.steps;

import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.model.common.Credentials;
import io.cucumber.java.en.And;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class TrainingSteps {

    private static final String TRAINING_URL = "/api/v1/trainings";
    private static final String PORT_PROPERTY = "local.server.port";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SharedTestContext sharedContext;

    @Autowired
    private Environment environment;

    @When("the client creates a training with name {string} and duration {int} and date {string}")
    public void createTraining(String name, int duration, String dateStr) {
        Credentials trainee = sharedContext.getCredentials();
        Credentials trainer = sharedContext.getTrainerCredentials();

        TrainingCreateRequest request = new TrainingCreateRequest(
                trainee.getUsername(),
                trainer.getUsername(),
                name,
                parseDate(dateStr),
                duration);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    baseUrl(TRAINING_URL), HttpMethod.POST,
                    new HttpEntity<>(request, authHeaders()), Void.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @And("a training exists with name {string} and duration {int} and date {string}")
    public void trainingExists(String name, int duration, String dateStr) {
        createTraining(name, duration, dateStr);
    }

    @When("the client requests trainee trainings for the registered trainee")
    public void getTraineeTrainings() {
        String username = sharedContext.getCredentials().getUsername();
        try {
            ResponseEntity<List> response = restTemplate.exchange(
                    baseUrl(TRAINING_URL + "/trainee/" + username),
                    HttpMethod.GET, new HttpEntity<>(authHeaders()), List.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("the client requests trainer trainings for the registered trainer")
    public void getTrainerTrainings() {
        String username = sharedContext.getTrainerCredentials().getUsername();
        try {
            ResponseEntity<List> response = restTemplate.exchange(
                    baseUrl(TRAINING_URL + "/trainer/" + username),
                    HttpMethod.GET, new HttpEntity<>(authHeaders()), List.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    @When("the client creates a training without auth")
    public void createTrainingWithoutAuth() {
        TrainingCreateRequest request = new TrainingCreateRequest(
                "any.user",
                "any.trainer",
                "Test",
                parseDate("2025-06-15"),
                30);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    baseUrl(TRAINING_URL), HttpMethod.POST,
                    new HttpEntity<>(request, headers), Void.class);
            sharedContext.setLastResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            sharedContext.setLastResponse(ResponseEntity.status(e.getStatusCode()).build());
        }
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
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
