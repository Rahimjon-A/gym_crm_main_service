package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    private static final String BASE_URL = "/api/v1/auth";
    private static final String URL_TRAINEE_REG = BASE_URL + "/trainee/registration";
    private static final String URL_TRAINER_REG = BASE_URL + "/trainer/registration";
    private static final String URL_LOGIN = BASE_URL + "/login";

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "securePassword123";
    private static final Long TYPE_ID = 1L;

    private static final String JSON_PATH_USERNAME = "$.username";
    private static final String JSON_PATH_PASSWORD = "$.password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymFacade gymFacade;

    @MockitoBean
    private AuthService authService;

    private TraineeDTO traineeRequest;
    private TrainerDTO trainerRequest;
    private Credentials credentials;
    private Trainee mockTrainee;
    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        traineeRequest = new TraineeDTO();
        traineeRequest.setFirstName(FIRST_NAME);
        traineeRequest.setLastName(LAST_NAME);

        trainerRequest = new TrainerDTO();
        trainerRequest.setFirstName(FIRST_NAME);
        trainerRequest.setLastName(LAST_NAME);
        trainerRequest.setSpecializationId(TYPE_ID);

        credentials = new Credentials(USERNAME, PASSWORD);

        mockTrainee = new Trainee();
        mockTrainee.setUsername(USERNAME);
        mockTrainee.setPassword(PASSWORD);

        mockTrainer = new Trainer();
        mockTrainer.setUsername(USERNAME);
        mockTrainer.setPassword(PASSWORD);
    }

    @Test
    void registerTrainee_shouldReturn201AndCredentials_whenRequestIsValid() throws Exception {
        when(gymFacade.createTrainee(any(TraineeDTO.class))).thenReturn(mockTrainee);

        mockMvc.perform(post(URL_TRAINEE_REG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest))) // Convert DTO to JSON body
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(USERNAME))
                .andExpect(jsonPath(JSON_PATH_PASSWORD).value(PASSWORD));

        verify(gymFacade, times(1)).createTrainee(any(TraineeDTO.class));
    }

    @Test
    void registerTrainer_shouldReturn201AndCredentials_whenRequestIsValid() throws Exception {
        when(gymFacade.createTrainer(any(TrainerDTO.class))).thenReturn(mockTrainer);

        mockMvc.perform(post(URL_TRAINER_REG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(USERNAME))
                .andExpect(jsonPath(JSON_PATH_PASSWORD).value(PASSWORD));

        verify(gymFacade, times(1)).createTrainer(any(TrainerDTO.class));
    }

    @Test
    void login_shouldReturn200_whenCredentialsAreValid() throws Exception {
        doNothing().when(gymFacade).login(any(Credentials.class));

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).login(any(Credentials.class));
    }

    @Test
    void registerTrainee_shouldReturn400_whenFirstNameIsBlank() throws Exception {
        traineeRequest.setFirstName("");

        mockMvc.perform(post(URL_TRAINEE_REG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest)))
                .andExpect(status().isBadRequest());

        verify(gymFacade, never()).createTrainee(any());
    }
}
