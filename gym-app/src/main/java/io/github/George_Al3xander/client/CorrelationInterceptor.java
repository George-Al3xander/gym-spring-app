package io.github.George_Al3xander.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CorrelationInterceptor implements RequestInterceptor {
    @Value("${correlation-id.header}")
    private String correlationIdKey;

    @Override
    public void apply(RequestTemplate template) {
        String correlationId = MDC.get(correlationIdKey);

        if (correlationId != null && !correlationId.isBlank()) {
            template.header(correlationIdKey, correlationId);
        }
    }
}
