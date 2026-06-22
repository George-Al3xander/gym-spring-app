package io.github.George_Al3xander.storage;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryStorage implements Storage {

    @Autowired
    private Map<Long, Trainee> traineeStorage;
    @Autowired
    private Map<Long, Trainer> trainerStorage;
    @Autowired
    private Map<Long, Training> trainingStorage;


    public Map<Long, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<Long, Trainer> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<Long, Training> getTrainingStorage() {
        return trainingStorage;
    }
}