package epam.com.gym.crm.controller;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.response.trainee.TraineeTrainingResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerTrainingResponse;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainingMapper;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.AuthService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
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
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingController.class)
@Import(TrainingControllerTest.MetricsConfig.class)
class TrainingControllerTest {
    private static final String BASE_URL = "/api/v1/trainings";
    private static final String URL_TRAINEE_TRAININGS = BASE_URL + "/trainee/{username}";
    private static final String URL_TRAINER_TRAININGS = BASE_URL + "/trainer/{username}";

    private static final String USERNAME = "john.doe";
    private static final String TRAINER_NAME = "jane.smith";
    private static final String TRAINING_NAME = "Morning Cardio";
    private static final String TRAINING_TYPE = "CARDIO";
    private static final Double DURATION = 60.0;
    
    private static final String PARAM_PERIOD_FROM = "periodFrom";
    private static final String PARAM_PERIOD_TO = "periodTo";
    private static final String PARAM_TRAINER_NAME = "trainerName";
    private static final String PARAM_TRAINEE_NAME = "traineeName";
    private static final String PARAM_TRAINING_TYPE = "trainingType";

    private static final String DATE_FROM_STR = "2026-03-01";
    private static final String DATE_TO_STR = "2026-03-31";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = USERNAME + ":password123";
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    private static final String JSON_PATH_ROOT_ARRAY = "$";
    private static final String JSON_PATH_TRAINING_NAME = "$[0].trainingName";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymFacade gymFacade;

    @MockitoBean
    private TrainingMapper trainingMapper;

    @MockitoBean
    private AuthService authService;

    private Training mockTraining;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();
        mockTraining = new Training();
        mockTraining.setTrainingName(TRAINING_NAME);

        doNothing().when(authService).authenticate(any());
    }

    @Test
    void addTraining_shouldReturn200_whenRequestIsValid() throws Exception {
        TrainingCreateRequest requestBody = new TrainingCreateRequest();
        requestBody.setTraineeUsername(USERNAME);
        requestBody.setTrainerUsername(TRAINER_NAME);
        requestBody.setTrainingName(TRAINING_NAME);
        requestBody.setTrainingDate(now);
        requestBody.setTrainingDuration(DURATION);

        when(gymFacade.createTraining(any(TrainingCreateRequest.class))).thenReturn(mockTraining);

        mockMvc.perform(post(BASE_URL)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).createTraining(any(TrainingCreateRequest.class));
    }

    @Test
    void getTraineeTrainings_shouldReturn200AndList_whenUsingFilters() throws Exception {
        TraineeTrainingResponse responseDTO = new TraineeTrainingResponse();
        responseDTO.setTrainingName(TRAINING_NAME);
        List<TraineeTrainingResponse> expectedResponse = List.of(responseDTO);

        when(gymFacade.getTraineeTrainingsByCriteria(any(TraineeTrainingFilter.class))).thenReturn(List.of(mockTraining));
        when(trainingMapper.mapTraineeTrainings(anyList())).thenReturn(expectedResponse);

        mockMvc.perform(get(URL_TRAINEE_TRAININGS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_PERIOD_FROM, DATE_FROM_STR)
                        .param(PARAM_PERIOD_TO, DATE_TO_STR)
                        .param(PARAM_TRAINER_NAME, TRAINER_NAME)
                        .param(PARAM_TRAINING_TYPE, TRAINING_TYPE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT_ARRAY).isArray())
                .andExpect(jsonPath(JSON_PATH_TRAINING_NAME).value(TRAINING_NAME));

        verify(gymFacade, times(1)).getTraineeTrainingsByCriteria(any(TraineeTrainingFilter.class));
    }

    @Test
    void getTrainerTrainings_shouldReturn200AndList_whenUsingFilters() throws Exception {
        TrainerTrainingResponse responseDTO = new TrainerTrainingResponse();
        responseDTO.setTrainingName(TRAINING_NAME);
        List<TrainerTrainingResponse> expectedResponse = List.of(responseDTO);

        when(gymFacade.getTrainerTrainingsByCriteria(any(TrainerTrainingFilter.class))).thenReturn(List.of(mockTraining));
        when(trainingMapper.mapTrainerTrainings(anyList())).thenReturn(expectedResponse);

        mockMvc.perform(get(URL_TRAINER_TRAININGS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_PERIOD_FROM, DATE_FROM_STR)
                        .param(PARAM_PERIOD_TO, DATE_TO_STR)
                        .param(PARAM_TRAINEE_NAME, USERNAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT_ARRAY).isArray())
                .andExpect(jsonPath(JSON_PATH_TRAINING_NAME).value(TRAINING_NAME));

        verify(gymFacade, times(1)).getTrainerTrainingsByCriteria(any(TrainerTrainingFilter.class));
    }

    @TestConfiguration
    static class MetricsConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}
