package epam.com.gym.crm.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenExtractorTest {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TOKEN = "Bearer valid.mock.jwt";
    private static final String INVALID_HEADER = "Basic dXNlcjpwYXNz";

    @InjectMocks
    private JwtTokenExtractor jwtTokenExtractor;

    @Mock
    private HttpServletRequest request;

    @Test
    void extractBearerToken_shouldReturnFullHeader_whenBearerPresent() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(BEARER_TOKEN);

        String result = jwtTokenExtractor.extractBearerToken(request);

        assertNotNull(result);
        assertEquals(BEARER_TOKEN, result);
    }

    @Test
    void extractBearerToken_shouldReturnNull_whenHeaderMissing() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        String result = jwtTokenExtractor.extractBearerToken(request);

        assertNull(result);
    }

    @Test
    void extractBearerToken_shouldReturnNull_whenHeaderDoesNotStartWithBearer() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(INVALID_HEADER);

        String result = jwtTokenExtractor.extractBearerToken(request);

        assertNull(result);
    }

    @Test
    void extractBearerToken_shouldReturnNull_whenHeaderIsEmpty() {
        when(request.getHeader(AUTHORIZATION)).thenReturn("");

        String result = jwtTokenExtractor.extractBearerToken(request);

        assertNull(result);
    }
}
