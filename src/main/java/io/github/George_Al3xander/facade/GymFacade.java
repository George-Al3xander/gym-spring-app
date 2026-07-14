package io.github.George_Al3xander.facade;

import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;

import java.util.List;

public interface GymFacade {

    Trainer createTrainer(Trainer trainer);

    Trainee createTrainee(TraineeRegistrationRequest trainee);

    Trainer getTrainer(CredentialsDTO credentials, String trainerUsername);

    Trainee getTrainee(CredentialsDTO credentials, String traineeUsername);

    void resetUserPassword(CredentialsDTO credentials, Long id);

    Trainer updateTrainer(CredentialsDTO credentials, Trainer trainer);

    Trainee updateTrainee(CredentialsDTO credentials, Trainee trainee);

    void toggleUserActiveStatus(CredentialsDTO credentials, String username);

    void deleteTrainee(CredentialsDTO credentials, String traineeUsername);

    List<Training> getTraineeTrainings(CredentialsDTO credentials, TrainingFilter criteria);

    List<Training> getTrainerTrainings(CredentialsDTO credentials, TrainingFilter criteria);

    Training addTraining(CredentialsDTO credentials, Training training);

    List<Trainer> getUnassignedTrainers(CredentialsDTO credentials, String traineeUsername);
}