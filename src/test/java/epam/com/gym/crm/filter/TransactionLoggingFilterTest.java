package epam.com.gym.crm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionLoggingFilterTest {
    private static final String URI = "/api/v1/test";
    private static final String METHOD = "GET";
    private static final String ERROR_BODY = "{\"error\": \"Bad Request\"}";

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TransactionLoggingFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setRequestURI(URI);
        request.setMethod(METHOD);

        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void doFilterInternal_shouldExecuteChainAndCopyBody_whenSuccessful() throws ServletException, IOException {
        response.setStatus(200);
        response.getWriter().write("Success Response");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(any(), any());
        
        assertNull(MDC.get(TransactionLoggingFilter.TRANSACTION_ID_KEY));

        assertEquals("Success Response", response.getContentAsString());
    }

    @Test
    void doFilterInternal_shouldExecuteChainAndLogBody_whenErrorStatus() throws ServletException, IOException {
        response.setStatus(400);
        response.getWriter().write(ERROR_BODY);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(any(), any());
        assertNull(MDC.get(TransactionLoggingFilter.TRANSACTION_ID_KEY));
        
        assertEquals(ERROR_BODY, response.getContentAsString());
    }

    @Test
    void doFilterInternal_shouldCleanUpMdc_evenWhenChainThrowsException() throws ServletException, IOException {
        doThrow(new ServletException("Chain failed!")).when(filterChain).doFilter(any(), any());

        assertThrows(ServletException.class, () -> filter.doFilterInternal(request, response, filterChain));

        assertNull(MDC.get(TransactionLoggingFilter.TRANSACTION_ID_KEY));
    }
}
