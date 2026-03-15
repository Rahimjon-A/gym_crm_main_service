package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.dto.trainee.TraineeResponseDTO;
import epam.com.gym.crm.dto.trainee.TraineeTrainerUpdateListDTO;
import epam.com.gym.crm.dto.trainee.TraineeUpdateDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TraineeMapper;
import epam.com.gym.crm.model.Trainee;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TraineeController.class)
class TraineeControllerTest {
    private static final String BASE_URL = "/api/v1/trainees";
    private static final String URL_PROFILE = BASE_URL + "/{username}";
    private static final String URL_TRAINERS = BASE_URL + "/{username}/trainers";
    private static final String URL_STATUS = BASE_URL + "/{username}/status";
    private static final String URL_PASSWORD = BASE_URL + "/password";
    
    private static final String PARAM_IS_ACTIVE = "isActive";

    private static final String USERNAME = "john.doe";
    private static final String FIRST_NAME = "John";
    private static final String NEW_PASSWORD = "newSecurePassword123";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = USERNAME + ":oldPassword123";
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    private static final String JSON_PATH_FIRST_NAME = "$.firstName";
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
    private TraineeResponseDTO profileResponse;
    private TraineeUpdateDTO updateResponse;

    @BeforeEach
    void setUp() {
        mockTrainee = new Trainee();
        mockTrainee.setUsername(USERNAME);
        mockTrainee.setFirstName(FIRST_NAME);

        profileResponse = new TraineeResponseDTO();
        profileResponse.setFirstName(FIRST_NAME);

        updateResponse = new TraineeUpdateDTO();
        updateResponse.setFirstName(FIRST_NAME);

        doNothing().when(authService).authenticate(any());
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
        TraineeDTO requestBody = new TraineeDTO();
        requestBody.setFirstName(FIRST_NAME);
        requestBody.setLastName("Doe");

        when(gymFacade.updateTrainee(eq(USERNAME), any(TraineeDTO.class))).thenReturn(mockTrainee);
        when(traineeMapper.toUpdateResponse(mockTrainee)).thenReturn(updateResponse);

        mockMvc.perform(put(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME));

        verify(gymFacade, times(1)).updateTrainee(eq(USERNAME), any(TraineeDTO.class));
    }

    @Test
    void updateTraineeTrainers_shouldReturn200AndTrainerList() throws Exception {
        TraineeTrainerUpdateListDTO requestBody = new TraineeTrainerUpdateListDTO(
                List.of(new epam.com.gym.crm.dto.trainer.TrainerAssignmentDTO(10L, "jane.smith"))
        );

        List<TrainerShortDTO> expectedResponse = List.of(new TrainerShortDTO("jane.smith", "Jane", "Smith", "YOGA"));

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
    void deleteTraineeProfile_shouldReturn200() throws Exception {
        doNothing().when(gymFacade).deleteTrainee(USERNAME);

        mockMvc.perform(delete(URL_PROFILE, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).deleteTrainee(USERNAME);
    }

    @Test
    void toggleTraineeStatus_shouldReturn200_whenActivating() throws Exception {
        doNothing().when(gymFacade).activateTrainee(USERNAME);

        mockMvc.perform(patch(URL_STATUS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_IS_ACTIVE, String.valueOf(true)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).activateTrainee(USERNAME);
        verify(gymFacade, never()).deactivateTrainee(anyString());
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
