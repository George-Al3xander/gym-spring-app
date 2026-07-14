package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.mapper.TraineeMapper;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.*;
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

    private final AuthenticationService authenticationService;

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
    public Trainer getTrainer(CredentialsDTO credentials, String trainerUsername) {
        authenticate(credentials);

        return trainerService.getTrainerByUsername(trainerUsername);
    }

    @Override
    public Trainee getTrainee(CredentialsDTO credentials, String traineeUsername) {
        authenticate(credentials);

        return traineeService.getTraineeByUsername(traineeUsername);

    }

    @Override
    public void resetUserPassword(CredentialsDTO credentials, Long id) {
        authenticate(credentials);

        userService.resetPassword(id);
    }

    @Override
    public Trainer updateTrainer(CredentialsDTO credentials, Trainer trainer) {
        authenticate(credentials);

        return trainerService.updateTrainer(trainer);
    }

    @Override
    public Trainee updateTrainee(CredentialsDTO credentials, Trainee trainee) {
        authenticate(credentials);

        return traineeService.updateTrainee(trainee);
    }

    @Override
    public void toggleUserActiveStatus(CredentialsDTO credentials, String username) {
        authenticate(credentials);

        userService.toggleActiveStatusByUsername(username);
    }

    @Override
    public void deleteTrainee(CredentialsDTO credentials, String traineeUsername) {
        authenticate(credentials);

        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        traineeService.deleteTrainee(trainee.getId());
    }

    @Override
    public List<Training> getTraineeTrainings(CredentialsDTO credentials, TrainingFilter criteria) {
        authenticate(credentials);

        return trainingService.findByTraineeUsername(credentials.getUsername(), criteria);
    }

    @Override
    public List<Training> getTrainerTrainings(CredentialsDTO credentials, TrainingFilter criteria) {
        authenticate(credentials);

        return trainingService.findByTrainerUsername(credentials.getUsername(), criteria);
    }

    @Override
    public Training addTraining(CredentialsDTO credentials, Training training) {
        authenticate(credentials);

        return trainingService.saveTraining(training);
    }

    @Override
    public List<Trainer> getUnassignedTrainers(CredentialsDTO credentials, String traineeUsername) {
        authenticate(credentials);

        return trainerService.getUnassignedTrainersByTraineeUsername(traineeUsername);
    }

    private void authenticate(CredentialsDTO credentials) {
        boolean allowed = authenticationService.authenticate(credentials);

        if (!allowed) {
            throw new BadCredentialsException("Invalid username or password.");
        }
    }
}
