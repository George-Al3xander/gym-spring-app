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
    private Map<String, Trainee> traineeStorage;
    @Autowired
    private Map<String, Trainer> trainerStorage;
    @Autowired
    private Map<String, Training> trainingStorage;


    public Map<String, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<String, Trainer> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<String, Training> getTrainingStorage() {
        return trainingStorage;
    }
}