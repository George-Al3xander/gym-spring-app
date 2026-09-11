package io.github.George_Al3xander.config;

import io.github.George_Al3xander.service.TrainerWorkloadService;
import io.github.common.dto.trainer.TrainerWorkloadRequest;
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
@RequiredArgsConstructor
@Slf4j
public class MessagingConfig {

    private final TrainerWorkloadService trainerWorkloadService;

    @Value("${correlation-id.header}")
    private String correlationIdKey;

    @JmsListener(destination = "${app.trainer-workload.queue-name}")
    public void receiveWorkload(
            TrainerWorkloadRequest workloadRequest
    ) {
        String correlationId = workloadRequest.getCorrelationId();

        try {
            MDC.put(correlationIdKey, correlationId);
            validateWorkloadRequest(workloadRequest);
            trainerWorkloadService.handleTraining(workloadRequest);
        } catch (Exception e) {
            log.error("Processing failed", e);
            throw e;
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
                "trainer.workload", TrainerWorkloadRequest.class
        ));

        return converter;
    }

    private void validateWorkloadRequest(TrainerWorkloadRequest workloadRequest) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TrainerWorkloadRequest>> violations = validator.validate(workloadRequest);

            if (!violations.isEmpty()) {
                throw new IllegalArgumentException(
                        "Invalid WorkloadRequest: " + violations
                );
            }
        }
    }
}
