package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Trainee;

import java.util.List;

public interface TraineeService {

    Trainee getTraineeById(String id);

    List<Trainee> getAllTrainees(String id);

    Trainee saveTrainee(Trainee entity);

    Trainee updateTrainee(Trainee entity);

    void deleteTrainee(String id);
}
