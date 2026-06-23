package io.github.George_Al3xander.config;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfig {

    private static final Logger log = LoggerFactory.getLogger(StorageConfig.class);

    @Value("${storage.file.path}")
    private String filePath;

    private final Map<String, Trainee> traineeStorage = new ConcurrentHashMap<>();
    private final Map<String, Trainer> trainerStorage = new ConcurrentHashMap<>();
    private final Map<String, Training> trainingStorage = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Initializing StorageConfig. Loading data from file: {}", filePath);
        preloadData();
        log.info("StorageConfig initialized successfully");
    }

    @Bean
    public Map<String, Trainee> traineeStorage() {
        return traineeStorage;
    }

    @Bean
    public Map<String, Trainer> trainerStorage() {
        return trainerStorage;
    }

    @Bean
    public Map<String, Training> trainingStorage() {
        return trainingStorage;
    }

    private void preloadData() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            File file = new File(filePath);

            GymData data = mapper.readValue(file, GymData.class);

            int traineesCount = data.trainees != null ? data.trainees.size() : 0;
            int trainersCount = data.trainers != null ? data.trainers.size() : 0;
            int trainingsCount = data.trainings != null ? data.trainings.size() : 0;

            if (traineesCount == 0 && trainersCount == 0 && trainingsCount == 0) {
                log.warn("Storage file '{}' contains no data.", filePath);
            }

            if (data.trainees != null) {
                traineeStorage.putAll(data.trainees);
            }
            if (data.trainers != null) {
                trainerStorage.putAll(data.trainers);
            }
            if (data.trainings != null) {
                trainingStorage.putAll(data.trainings);
            }

            log.info("Loaded storage successfully from '{}': trainees={}, trainers={}, trainings={}",
                    filePath, traineesCount, trainersCount, trainingsCount);

        } catch (Exception e) {
            log.warn("Failed to load storage file '{}'. Initialization will stop. Reason: {}",
                    filePath, e.getMessage(), e);

            throw new RuntimeException(
                    String.format(
                            "Failed to initialize StorageConfig: unable to load gym data from file '%s'. " +
                                    "Check that the file exists, is readable, and contains valid JSON.",
                            filePath
                    ),
                    e
            );
        }
    }

    private static class GymData {
        public Map<String, Trainee> trainees;
        public Map<String, Trainer> trainers;
        public Map<String, Training> trainings;
    }
}