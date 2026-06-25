package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDao trainingDao;

    private TraineeService traineeService;

    private TrainerService trainerService;

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

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }
}