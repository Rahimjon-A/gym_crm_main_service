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
    public static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";
    private static final int CACHE_LIMIT = 10_000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String transactionId = request.getHeader(TRANSACTION_ID_HEADER);
        if (transactionId == null || transactionId.isBlank()) {
            transactionId = UUID.randomUUID().toString();
            log.debug("Generated new transactionId: {}", transactionId);
        } else {
            log.debug("Received transactionId from upstream: {}", transactionId);
        }

        MDC.put(TRANSACTION_ID_KEY, transactionId);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, CACHE_LIMIT);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            log.info("Incoming REST Call: [{}] {} | transactionId: {}",
                    request.getMethod(), request.getRequestURI(), transactionId);

            filterChain.doFilter(wrappedRequest, wrappedResponse);

            long duration = System.currentTimeMillis() - startTime;
            logResponse(wrappedResponse, request.getMethod(), wrappedRequest.getRequestURI(), duration);

        } finally {
            wrappedResponse.copyBodyToResponse();
            MDC.remove(TRANSACTION_ID_KEY);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response, String method, String uri, long duration) {
        int statusCode = response.getStatus();
        HttpStatus status = HttpStatus.valueOf(statusCode);

        if (status.is4xxClientError()) {
            String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.warn("Outgoing REST Client Error: [{}] {} {} in {}ms | body: {}",
                    statusCode, method, uri, duration, body);

        } else if (status.is5xxServerError()) {
            String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.error("Outgoing REST Server Error: [{}] {} {} in {}ms | body: {}",
                    statusCode, method, uri, duration, body);

        } else {
            log.info("Outgoing REST Response: [{}] {} {} in {}ms",
                    statusCode, method, uri, duration);
        }
    }
}
