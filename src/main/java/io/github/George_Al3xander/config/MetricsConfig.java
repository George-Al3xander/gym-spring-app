package io.github.George_Al3xander.config;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsConfig {

    private final TrainingTypeDao trainingTypeDao;

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public Gauge trainingTypesTotalGauge(MeterRegistry registry) {
        return Gauge.builder(
                        "api_training_types_total",
                        trainingTypeDao,
                        (trainingTypeDao) -> trainingTypeDao.findAll().size()
                )
                .description("Current number of training types")
                .register(registry);
    }
}