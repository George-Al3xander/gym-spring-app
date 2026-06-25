package io.github.George_Al3xander.config;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.util.SequenceGenerator;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Map<Long, Trainee> traineeStorage = new ConcurrentHashMap<>();
    private final Map<Long, Trainer> trainerStorage = new ConcurrentHashMap<>();
    private final Map<Long, Training> trainingStorage = new ConcurrentHashMap<>();

    private SequenceGenerator sequenceGenerator;

    @PostConstruct
    public void init() {
        log.info("Initializing StorageConfig. Loading data from file: {}", filePath);
        preloadData();
        log.info("StorageConfig initialized successfully");
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
            File file = new File(filePath);

            if (!file.exists()) {
                throw new IllegalStateException(
                        "Storage file does not exist: " + file.getAbsolutePath()
                );
            }

            GymData data = mapper.readValue(file, GymData.class);

            traineeStorage.putAll(data.trainees);
            trainerStorage.putAll(data.trainers);
            trainingStorage.putAll(data.trainings);

            int traineesCount = data.trainees.size();
            int trainersCount = data.trainers.size();
            int trainingsCount = data.trainings.size();

            long traineeMax = data.trainees.keySet()
                    .stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0);

            long trainerMax = data.trainers.keySet()
                    .stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0);

            long trainingMax = data.trainings.keySet()
                    .stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0);

            long biggestId = Math.max(
                    Math.max(traineeMax, trainerMax),
                    trainingMax
            );

            sequenceGenerator.setCurrentSeq(biggestId);

            log.info(
                    "Loaded storage successfully from '{}': trainees={}, trainers={}, trainings={}, nextId={}",
                    filePath,
                    traineesCount,
                    trainersCount,
                    trainingsCount,
                    biggestId + 1
            );

        } catch (Exception e) {
            log.error(
                    "Failed to load storage file '{}'. Reason: {}",
                    filePath,
                    e.getMessage(),
                    e
            );

            throw new RuntimeException(
                    String.format(
                            "Failed to initialize storage from '%s'. " +
                                    "Check that the file exists, is readable, and contains valid JSON.",
                            filePath
                    ),
                    e
            );
        }
    }

    @Autowired
    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    private static class GymData {
        public Map<Long, Trainee> trainees = new ConcurrentHashMap<>();
        public Map<Long, Trainer> trainers = new ConcurrentHashMap<>();
        public Map<Long, Training> trainings = new ConcurrentHashMap<>();
    }
}