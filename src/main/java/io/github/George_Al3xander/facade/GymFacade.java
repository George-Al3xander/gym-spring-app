package io.github.George_Al3xander.facade;

import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;

import java.util.List;

public interface GymFacade {

    Trainer createTrainer(Trainer trainer);

    Trainee createTrainee(TraineeRegistrationRequest trainee);

    Trainer getTrainer(String trainerUsername);

    Trainee getTrainee(String traineeUsername);

    void resetUserPassword(Long id);

    Trainer updateTrainer(Trainer trainer);

    Trainee updateTrainee(Trainee trainee);

    void toggleUserActiveStatus(String username);

    void deleteTrainee(String traineeUsername);

    List<Training> getTraineeTrainings(String username, TrainingFilter criteria);

    List<Training> getTrainerTrainings(String username, TrainingFilter criteria);

    Training addTraining(Training training);

    List<Trainer> getUnassignedTrainers(String traineeUsername);
}