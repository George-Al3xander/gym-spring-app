package io.github.George_Al3xander.storage;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;

import java.util.Map;

public interface Storage {

    Map<String, Trainee> getTraineeStorage();

    Map<String, Trainer> getTrainerStorage();

    Map<String, Training> getTrainingStorage();
}
