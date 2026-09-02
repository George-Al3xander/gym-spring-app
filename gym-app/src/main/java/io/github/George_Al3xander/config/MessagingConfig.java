package io.github.George_Al3xander.config;

import io.github.George_Al3xander.dto.trainer.TrainerWorkloadRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Map;

@Configuration
public class MessagingConfig {

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
}
