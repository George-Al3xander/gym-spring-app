package io.github.George_Al3xander.config;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfig {
    private final Map<Long, Trainee> traineeStorage = new ConcurrentHashMap<>();
    private final Map<Long, Trainer> trainerStorage = new ConcurrentHashMap<>();
    private final Map<Long, Training> trainingStorage = new ConcurrentHashMap<>();

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return traineeStorage;
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return trainerStorage;
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return trainingStorage;
    }


}