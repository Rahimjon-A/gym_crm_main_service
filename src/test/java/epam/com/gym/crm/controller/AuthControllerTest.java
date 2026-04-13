package epam.com.gym.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.common.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private GymFacade gymFacade;

    @InjectMocks
    private AuthController authController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        Credentials credentials = new Credentials();
        credentials.setUsername("testuser");
        credentials.setPassword("password123");

        String expectedToken = "jwt-token-123";
        when(gymFacade.login(any(Credentials.class))).thenReturn(expectedToken);

        ResponseEntity<Map<String, String>> response = authController.login(credentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().get("token"));
        verify(gymFacade).login(credentials);
    }

    @Test
    void login_WithInvalidCredentials_ShouldThrowException() {
        Credentials credentials = new Credentials();
        credentials.setUsername("invaliduser");
        credentials.setPassword("wrongpass");

        when(gymFacade.login(any(Credentials.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> authController.login(credentials));
        verify(gymFacade).login(credentials);
    }

    @Test
    void login_WithNullCredentials_ShouldThrowException() {
        assertThrows(Exception.class, () -> authController.login(null));
        verify(gymFacade, never()).login(any());
    }

    @Test
    void logout_WithValidToken_ShouldLogout() throws Exception {
        String token = "Bearer valid-jwt-token";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);

        ResponseEntity<Void> response = authController.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gymFacade).logout("valid-jwt-token");
    }

    @Test
    void logout_WithNoAuthorizationHeader_ShouldStillReturnOk() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<Void> response = authController.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gymFacade, never()).logout(any());
    }

    @Test
    void logout_WithInvalidAuthorizationHeader_ShouldNotLogout() {
        when(request.getHeader("Authorization")).thenReturn("Invalid token");

        ResponseEntity<Void> response = authController.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gymFacade, never()).logout(any());
    }

    @Test
    void logout_WithBearerTokenWithoutSpace_ShouldNotLogout() {
        when(request.getHeader("Authorization")).thenReturn("Bearer");

        ResponseEntity<Void> response = authController.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gymFacade, never()).logout(any());
    }

    @Test
    void login_Endpoint_ShouldReturnOk() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setUsername("testuser");
        credentials.setPassword("password123");

        when(gymFacade.login(any(Credentials.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
