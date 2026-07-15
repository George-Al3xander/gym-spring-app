package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Trainee;

import java.util.List;

public interface TraineeService {

    Trainee getTraineeById(Long id);

    Trainee getTraineeByUsername(String username);

    List<Trainee> getAllTrainees();

    List<Trainee> getTraineesByTrainerUsername(String username, boolean assigned);

    Trainee saveTrainee(Trainee entity);

    Trainee updateTrainee(Trainee entity);

    void deleteTrainee(Long id);
}
