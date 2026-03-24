package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.dto.response.trainer.TrainerResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerUpdateResponse;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainerMapper;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {
    private static final String BASE_URL = "/api/v1/trainers";
    private static final String URL_PROFILE = BASE_URL + "/{username}";

    private static final String USERNAME = "jane.smith";
    private static final String PASSWORD = "securePassword123";
    private static final String FIRST_NAME = "Jane";
    private static final String LAST_NAME = "Smith";
    private static final Long SPECIALIZATION_ID = 1L;

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = USERNAME + ":" + PASSWORD;
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    private static final String JSON_PATH_FIRST_NAME = "$.firstName";
    private static final String JSON_PATH_USERNAME = "$.username";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymFacade gymFacade;

    @MockitoBean
    private TrainerMapper trainerMapper;

    @MockitoBean
    private AuthService authService;

    private Trainer mockTrainer;
    private TrainerResponse profileResponse;
    private TrainerUpdateResponse updateResponse;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setUsername(USERNAME);
        mockTrainer.setFirstName(FIRST_NAME);
        mockTrainer.setPassword(PASSWORD);

        profileResponse = new TrainerResponse();
        profileResponse.setFirstName(FIRST_NAME);

        updateResponse = new TrainerUpdateResponse();
        updateResponse.setFirstName(FIRST_NAME);

        doNothing().when(authService).authenticate(any());
    }

    @Test
    void registerTrainer_shouldReturn201AndCredentials_whenRequestIsValid() throws Exception {
        TrainerCreateRequest requestBody = new TrainerCreateRequest();
        requestBody.setFirstName(FIRST_NAME);
        requestBody.setLastName(LAST_NAME);
        requestBody.setSpecializationId(SPECIALIZATION_ID);

        when(gymFacade.createTrainer(any(TrainerCreateRequest.class))).thenReturn(mockTrainer);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(USERNAME))
                .andExpect(jsonPath("$.password").value(PASSWORD));

        verify(gymFacade, times(1)).createTrainer(any(TrainerCreateRequest.class));
    }

    @Test
    void getTrainerProfile_shouldReturn200AndProfile() throws Exception {
        when(gymFacade.getTrainerByUserName(USERNAME)).thenReturn(mockTrainer);
        when(trainerMapper.toProfileResponse(mockTrainer)).thenReturn(profileResponse);

        mockMvc.perform(get(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME));

        verify(gymFacade, times(1)).getTrainerByUserName(USERNAME);
    }

    @Test
    void updateTrainerProfile_shouldReturn200AndUpdatedProfile() throws Exception {
        TrainerUpdateRequest requestBody = new TrainerUpdateRequest();
        requestBody.setUsername("Jane.Smith");
        requestBody.setFirstName(FIRST_NAME);
        requestBody.setLastName(LAST_NAME);
        requestBody.setSpecializationId(SPECIALIZATION_ID);
        requestBody.setIsActive(true);

        when(gymFacade.updateTrainer(eq(USERNAME), any(TrainerUpdateRequest.class))).thenReturn(mockTrainer);
        when(trainerMapper.toUpdateResponse(mockTrainer)).thenReturn(updateResponse);

        mockMvc.perform(put(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME));

        verify(gymFacade, times(1)).updateTrainer(eq(USERNAME), any(TrainerUpdateRequest.class));
    }
}
