package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Training;

import java.util.List;

public interface TrainingService {
    Training getTrainingById(String id);

    List<Training> getAllTrainings();

    Training saveTraining(Training entity);
}
