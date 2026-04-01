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
import epam.com.gym.crm.filter.JwtAuthFilter;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TraineeController.class,
        properties = {"gym.metrics.trainee.timer=test.trainee.timer.metric"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
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

    private Trainee trainee;
    private TraineeResponse profileResponse;
    private TraineeUpdateResponse updateResponse;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setUsername(USERNAME);
        trainee.setFirstName(FIRST_NAME);
        trainee.setPassword(PASSWORD);

        profileResponse = new TraineeResponse();
        profileResponse.setFirstName(FIRST_NAME);

        updateResponse = new TraineeUpdateResponse();
        updateResponse.setFirstName(FIRST_NAME);

    }

    @Test
    void registerTrainee_shouldReturn201() throws Exception {
        TraineeCreateRequest request = new TraineeCreateRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);

        when(gymFacade.createTrainee(any())).thenReturn(trainee);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.password").value(PASSWORD));

        verify(gymFacade).createTrainee(any());
    }

    @Test
    void getProfile_shouldReturn200() throws Exception {
        when(gymFacade.getTraineeByUsername(USERNAME)).thenReturn(trainee);
        when(traineeMapper.toProfileResponse(trainee)).thenReturn(profileResponse);

        mockMvc.perform(get(URL_PROFILE, USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    void updateProfile_shouldReturn200() throws Exception {
        TraineeUpdateRequest request = new TraineeUpdateRequest();
        request.setUsername(USERNAME);
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setIsActive(true);

        when(gymFacade.updateTrainee(eq(USERNAME), any())).thenReturn(trainee);
        when(traineeMapper.toUpdateResponse(trainee)).thenReturn(updateResponse);

        mockMvc.perform(put(URL_PROFILE, USERNAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    void deleteProfile_shouldReturn200() throws Exception {
        doNothing().when(gymFacade).deleteTrainee(USERNAME);

        mockMvc.perform(delete(URL_PROFILE, USERNAME))
                .andExpect(status().isOk());

        verify(gymFacade).deleteTrainee(USERNAME);
    }

    @Test
    void updateTrainers_shouldReturnList() throws Exception {
        TraineeTrainerUpdateListRequest request =
                new TraineeTrainerUpdateListRequest(
                        List.of(new TrainerAssignmentRequest(1L, "trainer"))
                );

        when(gymFacade.updateTraineeTrainings(eq(USERNAME), anyList()))
                .thenReturn(Collections.emptyList());

        when(traineeMapper.extractTrainers(any()))
                .thenReturn(List.of(new TrainerShortResponse("trainer", "T", "S", "YOGA")));

        mockMvc.perform(put(URL_TRAINERS, USERNAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getUnassigned_shouldReturnList() throws Exception {
        when(gymFacade.getUnassignedTrainersOfTrainee(USERNAME))
                .thenReturn(Collections.emptyList());

        when(traineeMapper.toTrainerShortDTOList(anyList()))
                .thenReturn(List.of(new TrainerShortResponse("trainer", "T", "S", "YOGA")));

        mockMvc.perform(get(URL_UNASSIGNED, USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @TestConfiguration
    static class MetricsConfig {
        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}