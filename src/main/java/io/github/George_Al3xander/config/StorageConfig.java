package io.github.George_Al3xander.config;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfig {

    @Value("${storage.file.path}")
    private String filePath;

    private final Map<Long, Trainee> traineeStorage = new ConcurrentHashMap<>();
    private final Map<Long, Trainer> trainerStorage = new ConcurrentHashMap<>();
    private final Map<Long, Training> trainingStorage = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        preloadData();
    }


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

    private void preloadData() {
        ObjectMapper mapper = new ObjectMapper();

        try {

            GymData data = mapper.readValue(
                    new File(filePath),
                    GymData.class
            );

            traineeStorage.putAll(data.trainees);

            trainerStorage.putAll(data.trainers);

            trainingStorage.putAll(data.trainings);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Cannot load storage file: " + filePath,
                    e
            );
        }
    }

    private static class GymData {
        public Map<Long, Trainee> trainees;
        public Map<Long, Trainer> trainers;
        public Map<Long, Training> trainings;
    }
}