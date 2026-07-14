package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.mapper.TraineeMapper;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import io.github.George_Al3xander.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacadeImpl implements GymFacade {

    private final UserService userService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    private final TraineeMapper traineeMapper;

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerService.saveTrainer(trainer);
    }

    @Override
    public Trainee createTrainee(TraineeRegistrationRequest request) {
        Trainee trainee = traineeMapper.toTrainee(request);

        return traineeService.saveTrainee(trainee);
    }

    @Override
    public Trainer getTrainer(String trainerUsername) {
        return trainerService.getTrainerByUsername(trainerUsername);
    }

    @Override
    public Trainee getTrainee(String traineeUsername) {
        return traineeService.getTraineeByUsername(traineeUsername);
    }

    @Override
    public void resetUserPassword(Long id) {
        userService.resetPassword(id);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    @Override
    public void toggleUserActiveStatus(String username) {
        userService.toggleActiveStatusByUsername(username);
    }

    @Override
    public void deleteTrainee(String traineeUsername) {
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        traineeService.deleteTrainee(trainee.getId());
    }

    @Override
    public List<Training> getTraineeTrainings(String username, TrainingFilter criteria) {
        return trainingService.findByTraineeUsername(username, criteria);
    }

    @Override
    public List<Training> getTrainerTrainings(String username, TrainingFilter criteria) {
        return trainingService.findByTrainerUsername(username, criteria);
    }

    @Override
    public Training addTraining(Training training) {
        return trainingService.saveTraining(training);
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        return trainerService.getUnassignedTrainersByTraineeUsername(traineeUsername);
    }

}
