package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao trainingDao;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    @Override
    public Training getTrainingById(Long id) {
        Optional<Training> optionalTraining = trainingDao.findById(id);

        if (optionalTraining.isEmpty()) {
            throw new EntityNotFoundException("Training", id);
        }

        return optionalTraining.get();
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingDao.findAll();
    }

    @Override
    public Training saveTraining(Training entity) {
        trainerService.getTrainerById(entity.getTrainer().getId());
        traineeService.getTraineeById(entity.getTrainee().getId());

        return trainingDao.save(entity);
    }

    @Override
    public List<Training> findByTraineeUsername(String username, TrainingFilter filter) {
        return trainingDao.findByTraineeUsername(username, filter);
    }

    @Override
    public List<Training> findByTrainerUsername(String username, TrainingFilter filter) {
        return trainingDao.findByTrainerUsername(username, filter);
    }

    @Override
    public int deleteForTraineeByTrainerUsernames(String traineeUsername, List<String> trainerUsernames) {
        if (trainerUsernames == null || trainerUsernames.isEmpty()) return -1;

        return trainingDao.deleteForTraineeByTrainerUsernames(traineeUsername, trainerUsernames);
    }
}