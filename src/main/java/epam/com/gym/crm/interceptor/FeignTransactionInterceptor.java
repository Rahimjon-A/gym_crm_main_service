package epam.com.gym.crm.interceptor;

import epam.com.gym.crm.filter.TransactionLoggingFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignTransactionInterceptor implements RequestInterceptor {
    private static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";

    @Override
    public void apply(RequestTemplate template) {
        String transactionId = MDC.get(TransactionLoggingFilter.TRANSACTION_ID_KEY);

        if (transactionId != null) {
            template.header(TRANSACTION_ID_HEADER, transactionId);
            log.debug("Forwarding transactionId to workload service: {}", transactionId);
        } else {
            log.warn("No transactionId found in MDC, workload call will have no transactionId");
        }
    }
}
