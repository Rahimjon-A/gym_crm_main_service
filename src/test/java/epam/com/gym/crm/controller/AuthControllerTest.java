package epam.com.gym.crm.controller;

import epam.com.gym.crm.facade.GymFacade;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    private static final String URL_LOGIN = "/api/v1/auth";
    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "securePassword123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymFacade gymFacade;

    private Credentials credentials;

    @MockitoBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        credentials = new Credentials(USERNAME, PASSWORD);

        doNothing().when(authService).authenticate(any());
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
}
