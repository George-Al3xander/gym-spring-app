package io.github.George_Al3xander.config;

import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.service.TrainerWorkloadService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MessagingConfig {

    private final TrainerWorkloadService trainerWorkloadService;

    @Value("${correlation-id.header}")
    private String correlationIdKey;

    @JmsListener(destination = "${app.trainer-workload.queue-name}")
    public void receiveWorkload(
            WorkloadRequest workloadRequest
    ) {
        String correlationId = workloadRequest.getCorrelationId();

        try {
            MDC.put(correlationIdKey, correlationId);
            validateWorkloadRequest(workloadRequest);
            trainerWorkloadService.handleTraining(workloadRequest);
        } finally {
            MDC.remove(correlationIdKey);
        }
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJmsMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        converter.setTypeIdMappings(Map.of(
                "trainer.workload", WorkloadRequest.class
        ));

        return converter;
    }

    private void validateWorkloadRequest(WorkloadRequest workloadRequest) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<WorkloadRequest>> violations = validator.validate(workloadRequest);

            throw new RuntimeException(violations.toString());
        }
    }
}
