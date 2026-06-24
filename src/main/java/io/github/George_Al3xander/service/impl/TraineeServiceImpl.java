package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityInUseException;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.UsernameGenerator;
import io.github.George_Al3xander.util.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingDao trainingDao;

    private UsernameGenerator usernameGenerator;

    @Override
    public Trainee getTraineeById(String id) {
        log.info("Fetching trainee with id={}", id);

        Optional<Trainee> optionalTrainee = traineeDao.findById(id);

        if (optionalTrainee.isEmpty()) {
            EntityNotFoundException ex = new EntityNotFoundException("Trainee", id);
            log.warn(ex.getMessage());
            throw ex;
        }


        log.info("Trainee found, id={}", id);
        return optionalTrainee.get();
    }

    @Override
    public List<Trainee> getAllTrainees() {
        log.info("Fetching all trainees");

        List<Trainee> trainees = traineeDao.findAll();

        log.info("Retrieved {} trainees", trainees.size());
        return trainees;
    }

    @Override
    public Trainee saveTrainee(Trainee entity) {
        log.info(
                "Creating trainee firstName={} lastName={}",
                entity.getFirstName(),
                entity.getLastName()
        );

        entity.setUsername(usernameGenerator.generateUsername(entity));
        entity.setPassword(PasswordGenerator.generatePassword(10));

        Trainee savedTrainee = traineeDao.save(entity);

        log.info(
                "Trainee created successfully id={} username={}",
                savedTrainee.getUserId(),
                savedTrainee.getUsername()
        );

        return savedTrainee;
    }

    @Override
    public Trainee updateTrainee(Trainee entity) {
        log.info("Updating trainee id={}", entity.getUserId());

        Trainee updatedTrainee = traineeDao.save(entity);

        log.info("Trainee updated successfully id={}", updatedTrainee.getUserId());

        return updatedTrainee;
    }

    @Override
    public void deleteTrainee(String id) {
        log.info("Deleting trainee id={}", id);

        trainingDao.findAll()
                .stream()
                .filter(t -> t.getTraineeId().equalsIgnoreCase(id))
                .findFirst()
                .ifPresent(t -> {
                    throw new EntityInUseException(
                            "Trainee",
                            id,
                            "Training",
                            t.getTrainingName()
                    );
                });

        traineeDao.delete(id);

        log.info("Trainee deleted successfully id={}", id);
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }
}