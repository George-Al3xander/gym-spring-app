package io.github.George_Al3xander.config;

import io.github.common.dto.trainer.TrainerWorkloadRequest;
import jakarta.jms.Destination;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Map;

@Configuration
@Slf4j
public class MessagingConfig {

    @Value("${spring.activemq.broker.url}")
    private String brokerUrl;

    @Value("${correlation-id.header}")
    private String correlationIdKey;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);

        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveries(3);
        policy.setInitialRedeliveryDelay(1000);
        policy.setRedeliveryDelay(2000);
        policy.setUseExponentialBackOff(true);

        factory.setRedeliveryPolicy(policy);

        return factory;
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

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void handleDLQ(
            TrainerWorkloadRequest request,
            @Header(JmsHeaders.MESSAGE_ID) String messageId,
            @Header(JmsHeaders.DESTINATION) Destination destination) {

        try {
            MDC.put(correlationIdKey, request.getCorrelationId());
            log.error(
                    "DLQ message. messageId={}, destination={}, payload={}",
                    messageId,
                    destination,
                    request
            );
        } catch (Exception e) {
            log.error("Processing failed", e);
            throw e;
        } finally {
            MDC.remove(correlationIdKey);
        }
    }

}
