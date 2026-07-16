package io.github.George_Al3xander.facade;

import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.TraineeProfileResponse;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.dto.trainee.UpdateTraineeRequest;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.UpdateTrainerRequest;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;

import java.util.List;

public interface GymFacade {

    Trainer createTrainer(TrainerRegistrationRequest request);

    Trainee createTrainee(TraineeRegistrationRequest request);

    TrainerProfileResponse getTrainer(String trainerUsername);

    TraineeProfileResponse getTrainee(String traineeUsername);

    void resetUserPassword(Long id);

    TrainerProfileResponse updateTrainer(String username, UpdateTrainerRequest request);

    TraineeProfileResponse updateTrainee(String username, UpdateTraineeRequest request);

    void toggleUserActiveStatus(String username);

    void deleteTrainee(String traineeUsername);

    List<Training> getTraineeTrainings(String username, TrainingFilter criteria);

    List<Training> getTrainerTrainings(String username, TrainingFilter criteria);

    Training addTraining(Training training);

    List<Trainer> getUnassignedTrainers(String traineeUsername);
}