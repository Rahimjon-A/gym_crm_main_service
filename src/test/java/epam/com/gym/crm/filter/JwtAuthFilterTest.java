package epam.com.gym.crm.filter;

import epam.com.gym.crm.service.JwtService;
import epam.com.gym.crm.service.TokenBlacklistService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";
    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String BLACKLISTED_TOKEN = "blacklisted.jwt.token";
    private static final String EXPIRED_TOKEN = "expired.jwt.token";
    private static final String USERNAME = "john.doe";
    private static final String URL_SECURED = "/api/v1/trainings";

    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private TokenBlacklistService tokenBlacklistService;
    @Mock private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        request.setRequestURI(URL_SECURED);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldSkip_whenNoAuthorizationHeader()
            throws ServletException, IOException {
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService, tokenBlacklistService, userDetailsService);
    }

    @Test
    void doFilterInternal_shouldSkip_whenHeaderDoesNotStartWithBearer()
            throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, "Basic dXNlcjpwYXNz");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService, tokenBlacklistService, userDetailsService);
    }

    @Test
    void doFilterInternal_shouldReturn401_whenTokenIsBlacklisted()
            throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + BLACKLISTED_TOKEN);
        when(tokenBlacklistService.isBlacklisted(BLACKLISTED_TOKEN)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void doFilterInternal_shouldAuthenticate_whenTokenIsValid() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + VALID_TOKEN);

        when(tokenBlacklistService.isBlacklisted(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_TOKEN, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(List.of());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(userDetails, auth.getPrincipal());
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenTokenIsInvalid()
            throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + EXPIRED_TOKEN);

        when(tokenBlacklistService.isBlacklisted(EXPIRED_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(EXPIRED_TOKEN)).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenValid(EXPIRED_TOKEN, userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenUsernameIsNull() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + VALID_TOKEN);

        when(tokenBlacklistService.isBlacklisted(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_shouldNotOverrideExistingAuthentication() throws ServletException, IOException {
        UsernamePasswordAuthenticationToken existingAuth =
                new UsernamePasswordAuthenticationToken("existing.user", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + VALID_TOKEN);
        when(tokenBlacklistService.isBlacklisted(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals("existing.user",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_shouldContinueChain_whenJwtServiceThrows() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + VALID_TOKEN);

        when(tokenBlacklistService.isBlacklisted(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_TOKEN))
                .thenThrow(new RuntimeException("Malformed token"));

        assertDoesNotThrow(() ->
                jwtAuthFilter.doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldContinueChain_whenUserDetailsServiceThrows() throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + VALID_TOKEN);

        when(tokenBlacklistService.isBlacklisted(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME))
                .thenThrow(new UsernameNotFoundException("Not found"));

        assertDoesNotThrow(() ->
                jwtAuthFilter.doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldContinueFilterChain_afterSuccessfulAuth()
            throws ServletException, IOException {
        request.addHeader(HEADER_AUTHORIZATION, PREFIX_BEARER + VALID_TOKEN);

        when(tokenBlacklistService.isBlacklisted(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_TOKEN, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(List.of());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(filterChain.getRequest());
    }
}
