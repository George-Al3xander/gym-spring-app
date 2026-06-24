package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Autowired
    private TrainingDao trainingDao;

    private TraineeService traineeService;

    private TrainerService trainerService;

    @Override
    public Training getTrainingById(String id) {
        log.info("Fetching training with id={}", id);

        Optional<Training> optionalTraining = trainingDao.findById(id);

        if (optionalTraining.isEmpty()) {
            EntityNotFoundException ex = new EntityNotFoundException("Training", id);
            log.warn(ex.getMessage());
            throw ex;
        }

        log.info("Training found, id={}", id);
        return optionalTraining.get();
    }

    @Override
    public List<Training> getAllTrainings() {
        log.info("Fetching all trainings");

        List<Training> trainings = trainingDao.findAll();

        log.info("Retrieved {} trainings", trainings.size());
        return trainings;
    }

    @Override
    public Training saveTraining(Training entity) {
        log.info(
                "Creating training name={} trainerId={} traineeId={}",
                entity.getTrainingName(),
                entity.getTrainerId(),
                entity.getTraineeId()
        );

        trainerService.getTrainerById(entity.getTrainerId());
        traineeService.getTraineeById(entity.getTraineeId());

        Training savedTraining = trainingDao.save(entity);

        log.info(
                "Training created successfully name={}",
                savedTraining.getTrainingName()
        );

        return savedTraining;
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