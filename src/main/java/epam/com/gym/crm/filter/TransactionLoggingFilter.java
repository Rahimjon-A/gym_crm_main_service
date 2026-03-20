package epam.com.gym.crm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class TransactionLoggingFilter extends OncePerRequestFilter {

    public static final String TRANSACTION_ID_KEY = "transactionId";
    private static final int CACHE_LIMIT = 10_000;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String transactionId = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID_KEY, transactionId);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, CACHE_LIMIT);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            log.debug("Incoming REST Call: [{}] {}", request.getMethod(), request.getRequestURI());

            filterChain.doFilter(wrappedRequest, wrappedResponse);

            long duration = System.currentTimeMillis() - startTime;
            logResponse(wrappedResponse, wrappedRequest.getRequestURI(), duration);

        } finally {
            wrappedResponse.copyBodyToResponse();
            MDC.remove(TRANSACTION_ID_KEY);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response, String uri, long duration) {
        int statusCode = response.getStatus();
        HttpStatus status = HttpStatus.valueOf(statusCode);

        if (status.isError()) {
            byte[] responseArray = response.getContentAsByteArray();
            String responseBody = new String(responseArray, StandardCharsets.UTF_8);

            if (status.is4xxClientError()) {
                log.warn("Outgoing REST Client Error: [{}] {} in {}ms. Error Payload: {}", statusCode, uri, duration, responseBody);
            } else if (status.is5xxServerError()) {
                log.error("Outgoing REST Server Error: [{}] {} in {}ms. Error Payload: {}", statusCode, uri, duration, responseBody);
            }

        } else {
            log.debug("Outgoing REST Response: [{}] {} in {}ms.", statusCode, uri, duration);
        }
    }
}
