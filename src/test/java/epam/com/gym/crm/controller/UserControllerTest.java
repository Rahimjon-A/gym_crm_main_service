package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.filter.JwtAuthFilter;
import epam.com.gym.crm.service.AuthService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    private static final String BASE_URL = "/api/v1/users";
    private static final String URL_PASSWORD = BASE_URL + "/{username}/password";
    private static final String URL_STATUS = BASE_URL + "/{username}/status";

    private static final String PARAM_IS_ACTIVE = "isActive";

    private static final String USERNAME = "john.doe";
    private static final String OLD_PASSWORD = "oldPassword123";
    private static final String NEW_PASSWORD = "newSecurePassword123";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PLAIN_CREDS = USERNAME + ":" + OLD_PASSWORD;
    private static final String BASE64_CREDS = Base64.getEncoder().encodeToString(PLAIN_CREDS.getBytes());
    private static final String BASIC_AUTH_VALUE = "Basic " + BASE64_CREDS;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymFacade gymFacade;

    @MockitoBean
    private AuthService authService;

    @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
    private MeterRegistry meterRegistry;

    private PasswordChangeRequest passwordChangeRequest;

    @BeforeEach
    void setUp() {
        passwordChangeRequest = new PasswordChangeRequest(USERNAME, OLD_PASSWORD, NEW_PASSWORD);

    }

    @Test
    void changePassword_shouldReturn200_whenRequestIsValid() throws Exception {
        doNothing().when(gymFacade).changePassword(any(PasswordChangeRequest.class));

        mockMvc.perform(put(URL_PASSWORD, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).changePassword(any(PasswordChangeRequest.class));
    }

    @Test
    void toggleUserStatus_shouldReturn200_whenActivating() throws Exception {
        doNothing().when(gymFacade).activateUser(USERNAME);

        mockMvc.perform(patch(URL_STATUS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_IS_ACTIVE, String.valueOf(true)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).activateUser(USERNAME);
        verify(gymFacade, never()).deactivateUser(anyString());
    }

    @Test
    void toggleUserStatus_shouldReturn200_whenDeactivating() throws Exception {
        doNothing().when(gymFacade).deactivateUser(USERNAME);

        mockMvc.perform(patch(URL_STATUS, USERNAME)
                        .header(AUTH_HEADER, BASIC_AUTH_VALUE)
                        .param(PARAM_IS_ACTIVE, String.valueOf(false)))
                .andExpect(status().isOk());

        verify(gymFacade, times(1)).deactivateUser(USERNAME);
        verify(gymFacade, never()).activateUser(anyString());
    }
}
