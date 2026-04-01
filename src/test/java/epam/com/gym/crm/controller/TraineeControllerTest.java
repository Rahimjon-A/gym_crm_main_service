package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeTrainerUpdateListRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeUpdateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.response.trainee.TraineeResponse;
import epam.com.gym.crm.dto.response.trainee.TraineeUpdateResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TraineeMapper;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.service.AuthService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TraineeController.class,
        properties = {"gym.metrics.trainee.timer=test.trainee.timer.metric"})
@Import(TraineeControllerTest.MetricsConfig.class)
class TraineeControllerTest {
    private static final String BASE_URL = "/api/v1/trainees";
    private static final String URL_PROFILE = BASE_URL + "/{username}";
    private static final String URL_TRAINERS = BASE_URL + "/{username}/trainers";
    private static final String URL_UNASSIGNED = BASE_URL + "/{username}/unassigned-trainers";

    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "plainPassword123";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = USERNAME + ":" + PASSWORD;
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    private static final String JSON_PATH_FIRST_NAME = "$.firstName";
    private static final String JSON_PATH_USERNAME = "$.username";
    private static final String JSON_PATH_ROOT_ARRAY = "$";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymFacade gymFacade;

    @MockitoBean
    private TraineeMapper traineeMapper;

    @MockitoBean
    private AuthService authService;

    private Trainee mockTrainee;
    private TraineeResponse profileResponse;
    private TraineeUpdateResponse updateResponse;

    @BeforeEach
    void setUp() {
        mockTrainee = new Trainee();
        mockTrainee.setUsername(USERNAME);
        mockTrainee.setFirstName(FIRST_NAME);
        mockTrainee.setPassword(PASSWORD);

        profileResponse = new TraineeResponse();
        profileResponse.setFirstName(FIRST_NAME);

        updateResponse = new TraineeUpdateResponse();
        updateResponse.setFirstName(FIRST_NAME);

        doNothing().when(authService).authenticate(any());
    }

    @Test
    void registerTrainee_shouldReturn201AndCredentials_whenRequestIsValid() throws Exception {
        TraineeCreateRequest requestBody = new TraineeCreateRequest();
        requestBody.setFirstName(FIRST_NAME);
        requestBody.setLastName(LAST_NAME);

        when(gymFacade.createTrainee(any(TraineeCreateRequest.class))).thenReturn(mockTrainee);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(USERNAME))
                .andExpect(jsonPath("$.password").value(PASSWORD));

        verify(gymFacade, times(1)).createTrainee(any(TraineeCreateRequest.class));
    }

    @Test
    void getTraineeProfile_shouldReturn200AndProfile() throws Exception {
        when(gymFacade.getTraineeByUsername(USERNAME)).thenReturn(mockTrainee);
        when(traineeMapper.toProfileResponse(mockTrainee)).thenReturn(profileResponse);

        mockMvc.perform(get(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME));

        verify(gymFacade, times(1)).getTraineeByUsername(USERNAME);
    }

    @Test
    void updateTraineeProfile_shouldReturn200AndUpdatedProfile() throws Exception {
        TraineeUpdateRequest requestBody = new TraineeUpdateRequest();
        requestBody.setUsername(USERNAME);
        requestBody.setFirstName(FIRST_NAME);
        requestBody.setLastName("Doe");
        requestBody.setIsActive(true);

        when(gymFacade.updateTrainee(eq(USERNAME), any(TraineeUpdateRequest.class))).thenReturn(mockTrainee);
        when(traineeMapper.toUpdateResponse(mockTrainee)).thenReturn(updateResponse);

        mockMvc.perform(put(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME));

        verify(gymFacade, times(1)).updateTrainee(eq(USERNAME), any(TraineeUpdateRequest.class));
    }

    @Test
    void deleteTraineeProfile_shouldReturn200() throws Exception {
        doNothing().when(gymFacade).deleteTrainee(USERNAME);

        mockMvc.perform(delete(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).deleteTrainee(USERNAME);
    }

    @Test
    void updateTraineeTrainers_shouldReturn200AndTrainerList() throws Exception {
        TraineeTrainerUpdateListRequest requestBody = new TraineeTrainerUpdateListRequest(
                List.of(new TrainerAssignmentRequest(10L, "jane.smith"))
        );

        List<TrainerShortResponse> expectedResponse = List.of(new TrainerShortResponse("jane.smith", "Jane", "Smith", "YOGA"));

        when(gymFacade.updateTraineeTrainings(eq(USERNAME), anyList())).thenReturn(Collections.emptyList());
        when(traineeMapper.extractTrainers(any(Trainee.class))).thenReturn(expectedResponse);

        mockMvc.perform(put(URL_TRAINERS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT_ARRAY).isArray());

        verify(gymFacade, times(1)).updateTraineeTrainings(eq(USERNAME), anyList());
    }

    @Test
    void getUnassignedTrainers_shouldReturn200AndTrainerList() throws Exception {
        List<TrainerShortResponse> expectedResponse = List.of(new TrainerShortResponse("jane.smith", "Jane", "Smith", "YOGA"));

        when(gymFacade.getUnassignedTrainersOfTrainee(USERNAME)).thenReturn(Collections.emptyList());
        when(traineeMapper.toTrainerShortDTOList(anyList())).thenReturn(expectedResponse);

        mockMvc.perform(get(URL_UNASSIGNED, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT_ARRAY).isArray());

        verify(gymFacade, times(1)).getUnassignedTrainersOfTrainee(USERNAME);
    }

    @TestConfiguration
    static class MetricsConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}
