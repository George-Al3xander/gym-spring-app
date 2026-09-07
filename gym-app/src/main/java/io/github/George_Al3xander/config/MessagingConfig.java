package io.github.George_Al3xander.config;

import io.github.George_Al3xander.dto.trainer.TrainerWorkloadRequest;
import jakarta.jms.Destination;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
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
            String json,
            @Header(JmsHeaders.MESSAGE_ID) String messageId,
            @Header("JMSDestination") Destination destination) {

        log.error(
                "DLQ message. messageId={}, destination={}, payload={}",
                messageId,
                destination,
                json
        );
    }

}
