package io.github.George_Al3xander.storage;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.util.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class StoragePreloadingBeanPostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(StoragePreloadingBeanPostProcessor.class);

    @Value("${storage.file.path}")
    private String filePath;

    private final SequenceGenerator sequenceGenerator;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof Storage storage) {
            preloadData(storage);
        }

        return bean;
    }

    private void preloadData(Storage storage) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                throw new IllegalStateException(
                        "Storage file does not exist: " + file.getAbsolutePath()
                );
            }

            GymData data = mapper.readValue(file, GymData.class);

            Map<Long, Trainee> traineeStorage = storage.getTraineeStorage();
            Map<Long, Trainer> trainerStorage = storage.getTrainerStorage();
            Map<Long, Training> trainingStorage = storage.getTrainingStorage();

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

    private static class GymData {
        public Map<Long, Trainee> trainees = new ConcurrentHashMap<>();
        public Map<Long, Trainer> trainers = new ConcurrentHashMap<>();
        public Map<Long, Training> trainings = new ConcurrentHashMap<>();
    }
}