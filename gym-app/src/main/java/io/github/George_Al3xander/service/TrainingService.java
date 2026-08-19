package io.github.George_Al3xander.service;

import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.model.Training;

import java.util.List;

public interface TrainingService {
    Training getTrainingById(Long id);

    List<Training> getAllTrainings();

    Training saveTraining(Training entity);

    List<Training> findByTraineeUsername(String username, TrainingFilter filter);

    List<Training> findByTrainerUsername(String username, TrainingFilter filter);

    int deleteForTraineeByTrainerUsernames(String traineeUsername, List<String> trainerUsernames);
}
