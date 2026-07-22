package io.github.George_Al3xander.facade;

import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.*;
import io.github.George_Al3xander.dto.trainer.*;
import io.github.George_Al3xander.dto.training.AddTrainingRequest;
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

    void updateActiveStatusByUsername(String username, boolean active);

    void deleteTrainee(String traineeUsername);

    List<TraineeTrainingResponse> getTraineeTrainings(String username, TrainingFilter criteria);

    List<TrainerTrainingResponse> getTrainerTrainings(String username, TrainingFilter criteria);

    Training addTraining(AddTrainingRequest request);

    List<TrainerSummaryResponse> getTrainersByTraineeUsername(String traineeUsername, TrainerFilter filter);

    List<TrainerSummaryResponse> updateTrainersListByTraineeUsername(String traineeUsername, UpdateTraineeTrainerListRequest request);
}