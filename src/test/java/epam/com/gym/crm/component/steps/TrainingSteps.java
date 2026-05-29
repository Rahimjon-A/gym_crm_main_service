package epam.com.gym.crm.component.steps;

import epam.com.gym.crm.component.context.SharedTestContext;
import epam.com.gym.crm.component.helper.RestHelper;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.model.common.Credentials;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class TrainingSteps {

    private static final String TRAINING_URL = "/api/v1/trainings";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private RestHelper restHelper;

    @Autowired
    private SharedTestContext sharedContext;

    @Autowired
    private Environment environment;

    @Before
    public void setUp() {
        restHelper = new RestHelper(environment);
    }

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

        ResponseEntity<Void> response = restHelper.execute(
                TRAINING_URL, HttpMethod.POST,
                new HttpEntity<>(request, restHelper.authHeaders(sharedContext.getJwtToken())),
                Void.class);
        sharedContext.setLastResponse(response);
    }

    @And("a training exists with name {string} and duration {int} and date {string}")
    public void trainingExists(String name, int duration, String dateStr) {
        createTraining(name, duration, dateStr);
    }

    @When("the client requests trainee trainings for the registered trainee")
    public void getTraineeTrainings() {
        String username = sharedContext.getCredentials().getUsername();
        ResponseEntity<List> response = restHelper
                .get(TRAINING_URL + "/trainee/" + username, sharedContext.getJwtToken(), List.class);
        sharedContext.setLastResponse(response);
    }

    @When("the client requests trainer trainings for the registered trainer")
    public void getTrainerTrainings() {
        String username = sharedContext.getTrainerCredentials().getUsername();
        ResponseEntity<List> response = restHelper
                .get(TRAINING_URL + "/trainer/" + username, sharedContext.getJwtToken(), List.class);
        sharedContext.setLastResponse(response);
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

        ResponseEntity<Void> response = restHelper.execute(
                TRAINING_URL, HttpMethod.POST,
                new HttpEntity<>(request), Void.class);
        sharedContext.setLastResponse(response);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }
}
