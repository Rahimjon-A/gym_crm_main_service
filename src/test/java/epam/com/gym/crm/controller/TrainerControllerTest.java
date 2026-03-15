package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
import epam.com.gym.crm.dto.trainer.TrainerResponseDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import epam.com.gym.crm.dto.trainer.TrainerUpdateDTO;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {
    private static final String BASE_URL = "/api/v1/trainers";
    private static final String URL_PROFILE = BASE_URL + "/{username}";
    private static final String URL_NOT_ASSIGNED = BASE_URL + "/not-assigned-on/{traineeUsername}";
    private static final String URL_STATUS = BASE_URL + "/{username}/status";
    private static final String URL_PASSWORD = BASE_URL + "/password";

    private static final String PARAM_IS_ACTIVE = "isActive";

    private static final String USERNAME = "jane.smith";
    private static final String FIRST_NAME = "Jane";
    private static final String LAST_NAME = "Smith";
    private static final String TRAINEE_USERNAME = "john.doe";
    private static final String NEW_PASSWORD = "newSecurePassword123";
    private static final Long SPECIALIZATION_ID = 1L;
    private static final String SPECIALIZATION_NAME = "YOGA";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = USERNAME + ":oldPassword123";
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    private static final String JSON_PATH_FIRST_NAME = "$.firstName";
    private static final String JSON_PATH_ROOT_ARRAY = "$";
    private static final String JSON_PATH_ARRAY_USERNAME = "$[0].username";

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
    private TrainerResponseDTO profileResponse;
    private TrainerUpdateDTO updateResponse;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setUsername(USERNAME);
        mockTrainer.setFirstName(FIRST_NAME);

        profileResponse = new TrainerResponseDTO();
        profileResponse.setFirstName(FIRST_NAME);

        updateResponse = new TrainerUpdateDTO();
        updateResponse.setFirstName(FIRST_NAME);

        doNothing().when(authService).authenticate(any());
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
        TrainerDTO requestBody = new TrainerDTO();
        requestBody.setFirstName(FIRST_NAME);
        requestBody.setLastName(LAST_NAME);
        requestBody.setSpecializationId(SPECIALIZATION_ID);

        when(gymFacade.updateTrainer(eq(USERNAME), any(TrainerDTO.class))).thenReturn(mockTrainer);
        when(trainerMapper.toUpdateResponse(mockTrainer)).thenReturn(updateResponse);

        mockMvc.perform(put(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME));

        verify(gymFacade, times(1)).updateTrainer(eq(USERNAME), any(TrainerDTO.class));
    }

    @Test
    void getUnassignedTrainers_shouldReturn200AndTrainerList() throws Exception {
        TrainerShortDTO shortDTO = new TrainerShortDTO(USERNAME, FIRST_NAME, LAST_NAME, SPECIALIZATION_NAME);
        List<TrainerShortDTO> expectedResponse = List.of(shortDTO);
        List<Trainer> unassignedTrainers = List.of(mockTrainer);

        when(gymFacade.getUnassignedTrainersOfTrainee(TRAINEE_USERNAME)).thenReturn(unassignedTrainers);
        when(trainerMapper.toShortDTOList(unassignedTrainers)).thenReturn(expectedResponse);

        mockMvc.perform(get(URL_NOT_ASSIGNED, TRAINEE_USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT_ARRAY).isArray())
                .andExpect(jsonPath(JSON_PATH_ARRAY_USERNAME).value(USERNAME));

        verify(gymFacade, times(1)).getUnassignedTrainersOfTrainee(TRAINEE_USERNAME);
    }

    @Test
    void toggleTrainerStatus_shouldReturn200_whenDeactivating() throws Exception {
        doNothing().when(gymFacade).deactivateTrainer(USERNAME);

        mockMvc.perform(patch(URL_STATUS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_IS_ACTIVE, String.valueOf(true)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).deactivateTrainer(USERNAME);
        verify(gymFacade, never()).activateTrainer(anyString());
    }

    @Test
    void toggleTrainerStatus_shouldReturn200_whenActivating() throws Exception {
        doNothing().when(gymFacade).activateTrainer(USERNAME);

        mockMvc.perform(patch(URL_STATUS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_IS_ACTIVE, String.valueOf(false)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).activateTrainer(USERNAME);
        verify(gymFacade, never()).deactivateTrainer(anyString());
    }

    @Test
    void changePassword_shouldReturn200() throws Exception {
        PasswordChangeRequest requestBody = new PasswordChangeRequest(USERNAME, "old", NEW_PASSWORD);
        doNothing().when(gymFacade).changePassword(any(PasswordChangeRequest.class));

        mockMvc.perform(put(URL_PASSWORD)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).changePassword(any(PasswordChangeRequest.class));
    }
}
