package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.TraineeProfileResponse;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.dto.trainee.TraineeSummaryResponse;
import io.github.George_Al3xander.dto.trainee.UpdateTraineeRequest;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.dto.trainer.UpdateTrainerRequest;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.mapper.TraineeMapper;
import io.github.George_Al3xander.mapper.TrainerMapper;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.model.TrainingType;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import io.github.George_Al3xander.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GymFacadeImpl implements GymFacade {

    private final UserService userService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final TrainingTypeDao trainingTypeDao;

    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;

    @Override
    public Trainer createTrainer(TrainerRegistrationRequest request) {
        Trainer trainer = trainerMapper.toTrainer(request);
        trainer.setSpecialization(getTrainingTypeById(request.getSpecializationId()));

        return trainerService.saveTrainer(trainer);
    }

    @Override
    public Trainee createTrainee(TraineeRegistrationRequest request) {
        Trainee trainee = traineeMapper.toTrainee(request);

        return traineeService.saveTrainee(trainee);
    }

    @Override
    public TrainerProfileResponse getTrainer(String trainerUsername) {
        Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);

        return trainerMapper.toTrainerProfile(trainer, getTraineesListByUsername(trainerUsername));
    }

    @Override
    public TraineeProfileResponse getTrainee(String traineeUsername) {
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);

        return traineeMapper.toTraineeProfile(trainee, getTrainersListByUsername(traineeUsername));
    }

    @Override
    public void resetUserPassword(Long id) {
        userService.resetPassword(id);
    }

    @Override
    public TrainerProfileResponse updateTrainer(String username, UpdateTrainerRequest request) {
        Trainer trainer = trainerService.getTrainerByUsername(username);

        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setIsActive(request.getIsActive());

        trainerService.updateTrainer(trainer);

        return trainerMapper.toTrainerProfile(trainer, getTraineesListByUsername(username));
    }

    @Override
    public TraineeProfileResponse updateTrainee(String username, UpdateTraineeRequest request) {
        Trainee trainee = traineeService.getTraineeByUsername(username);

        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setIsActive(request.getIsActive());

        if (request.getAddress() != null) {
            trainee.setAddress(request.getAddress());
        }

        if (request.getDateOfBirth() != null) {
            trainee.setDateOfBirth(request.getDateOfBirth());
        }

        traineeService.updateTrainee(trainee);

        return traineeMapper.toTraineeProfile(trainee, getTrainersListByUsername(username));
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
    public List<Trainer> getTrainersByTraineeUsername(String traineeUsername, TrainerFilter filter) {
        return trainerService.getTrainersByTraineeUsername(traineeUsername, filter);
    }

    private List<TrainerSummaryResponse> getTrainersListByUsername(String traineeUsername) {
        return trainerService
                .getTrainersByTraineeUsername(traineeUsername, null)
                .stream()
                .map(trainerMapper::toSummary)
                .toList();
    }

    private List<TraineeSummaryResponse> getTraineesListByUsername(String trainerUsername) {
        return traineeService
                .getTraineesByTrainerUsername(trainerUsername, true)
                .stream()
                .map(traineeMapper::toSummary)
                .toList();
    }

    private TrainingType getTrainingTypeById(Long id) {
        Optional<TrainingType> trainingTypeOptional = trainingTypeDao.findById(id);

        if (trainingTypeOptional.isEmpty()) {
            throw new EntityNotFoundException("Training type", id);
        }

        return trainingTypeOptional.get();
    }
}
