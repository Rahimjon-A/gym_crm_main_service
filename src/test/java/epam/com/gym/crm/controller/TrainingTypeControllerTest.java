package epam.com.gym.crm.controller;

import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingTypeController.class)
class TrainingTypeControllerTest {
    private static final String BASE_URL = "/api/v1/training-types";
    private static final Long TYPE_ID = 1L;
    private static final String TYPE_NAME = "YOGA";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = "test.user:password123";
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    private static final String JSON_PATH_ROOT = "$";
    private static final String JSON_PATH_FIRST_ID = "$[0].trainingTypeId";
    private static final String JSON_PATH_FIRST_NAME = "$[0].trainingType";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GymFacade gymFacade;

    @MockitoBean
    private AuthService authService;

    private TrainingType validTrainingType;

    @BeforeEach
    void setUp() {
        validTrainingType = new TrainingType();
        validTrainingType.setId(TYPE_ID);
        validTrainingType.setTrainingTypeName(TYPE_NAME);

        doNothing().when(authService).authenticate(any());
    }

    @Test
    void getAllTrainingTypes_shouldReturn200AndListOfTypes_whenAuthenticated() throws Exception {
        when(gymFacade.getAllTrainingTypes()).thenReturn(List.of(validTrainingType));

        mockMvc.perform(get(BASE_URL)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT).isArray())
                .andExpect(jsonPath(JSON_PATH_ROOT + ".length()").value(1))
                .andExpect(jsonPath(JSON_PATH_FIRST_ID).value(TYPE_ID))
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(TYPE_NAME));

        verify(gymFacade, times(1)).getAllTrainingTypes();
    }
}
