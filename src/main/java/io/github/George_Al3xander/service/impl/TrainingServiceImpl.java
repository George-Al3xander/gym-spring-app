package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
        trainerService.getTrainerById(entity.getTrainerId());
        traineeService.getTraineeById(entity.getTraineeId());

        return trainingDao.save(entity);
    }
}