package epam.com.gym.crm.filter;

import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String PREFIX_BASIC = "Basic ";

    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "password123";
    private static final String VALID_PLAIN_CREDS = USERNAME + ":" + PASSWORD;
    private static final String VALID_BASE64_CREDS = Base64.getEncoder().encodeToString(VALID_PLAIN_CREDS.getBytes());
    private static final String VALID_AUTH_HEADER = PREFIX_BASIC + VALID_BASE64_CREDS;

    private static final String INVALID_BASE64_CREDS = Base64.getEncoder().encodeToString("just_a_username_no_colon".getBytes());
    private static final String INVALID_AUTH_HEADER = PREFIX_BASIC + INVALID_BASE64_CREDS;

    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static final String URL_SECURED = "/api/v1/trainings";
    private static final String URL_TRAINEE_REG = "/api/v1/trainees";
    private static final String URL_TRAINER_REG = "/api/v1/trainers";
    private static final String URL_SWAGGER = "/swagger-ui/index.html";

    @Mock
    private AuthService authService;

    @Mock
    private HandlerExceptionResolver exceptionResolver;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void doFilterInternal_shouldAuthenticateAndProceed_whenCredentialsAreValid() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, VALID_AUTH_HEADER);
        request.setRequestURI(URL_SECURED);

        doNothing().when(authService).authenticate(any(Credentials.class));

        authenticationFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<Credentials> credentialsCaptor = ArgumentCaptor.forClass(Credentials.class);
        verify(authService, times(1)).authenticate(credentialsCaptor.capture());
        
        assertEquals(USERNAME, credentialsCaptor.getValue().getUsername());
        assertEquals(PASSWORD, credentialsCaptor.getValue().getPassword());
    }

    @Test
    void doFilterInternal_shouldResolveException_whenHeaderIsMissing() throws ServletException, IOException {
        request.setRequestURI(URL_SECURED);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(exceptionResolver, times(1)).resolveException(eq(request), eq(response), isNull(), any(AuthenticationException.class));
        verify(authService, never()).authenticate(any(Credentials.class));
    }

    @Test
    void doFilterInternal_shouldResolveException_whenHeaderLacksBasicPrefix() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, "Bearer some_token");
        request.setRequestURI(URL_SECURED);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(exceptionResolver, times(1)).resolveException(eq(request), eq(response), isNull(), any(AuthenticationException.class));
        verify(authService, never()).authenticate(any(Credentials.class));
    }

    @Test
    void doFilterInternal_shouldResolveException_whenCredentialsFormatIsInvalid() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, INVALID_AUTH_HEADER);
        request.setRequestURI(URL_SECURED);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(exceptionResolver, times(1)).resolveException(eq(request), eq(response), isNull(), any(AuthenticationException.class));
        verify(authService, never()).authenticate(any(Credentials.class));
    }

    @Test
    void doFilterInternal_shouldResolveException_whenBase64IsInvalid() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BASIC + "Not_Valid_Base64!@#");
        request.setRequestURI(URL_SECURED);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(exceptionResolver, times(1)).resolveException(eq(request), eq(response), isNull(), any(AuthenticationException.class));
        verify(authService, never()).authenticate(any(Credentials.class));
    }

    @Test
    void shouldNotFilter_shouldReturnTrue_forRegistrationEndpoints() {
        request.setMethod(METHOD_POST);
        
        request.setRequestURI(URL_TRAINEE_REG);
        assertTrue(authenticationFilter.shouldNotFilter(request));

        request.setRequestURI(URL_TRAINER_REG);
        assertTrue(authenticationFilter.shouldNotFilter(request));
    }

    @Test
    void shouldNotFilter_shouldReturnTrue_forSwaggerEndpoints() {
        request.setMethod(METHOD_GET);
        request.setRequestURI(URL_SWAGGER);
        
        assertTrue(authenticationFilter.shouldNotFilter(request));
    }

    @Test
    void shouldNotFilter_shouldReturnFalse_forSecuredEndpoints() {
        request.setMethod(METHOD_GET);
        request.setRequestURI(URL_SECURED);
        
        assertFalse(authenticationFilter.shouldNotFilter(request));
    }
}