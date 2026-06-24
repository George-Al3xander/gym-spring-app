package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.UsernameGenerator;
import io.github.George_Al3xander.util.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    private TrainerDao trainerDao;

    private UsernameGenerator usernameGenerator;

    @Override
    public Trainer getTrainerById(String id) {
        log.info("Fetching trainer with id={}", id);

        Optional<Trainer> optionalTrainer = trainerDao.findById(id);

        if (optionalTrainer.isEmpty()) {
            EntityNotFoundException ex = new EntityNotFoundException("Trainer", id);
            log.warn(ex.getMessage());
            throw ex;
        }

        log.info("Trainer found, id={}", id);
        return optionalTrainer.get();
    }

    @Override
    public List<Trainer> getAllTrainers() {
        log.info("Fetching all trainers");

        List<Trainer> trainers = trainerDao.findAll();

        log.info("Retrieved {} trainers", trainers.size());
        return trainers;
    }

    @Override
    public Trainer saveTrainer(Trainer entity) {
        log.info(
                "Creating trainer firstName={} lastName={}",
                entity.getFirstName(),
                entity.getLastName()
        );

        entity.setUsername(usernameGenerator.generateUsername(entity));
        entity.setPassword(PasswordGenerator.generatePassword(10));

        Trainer savedTrainer = trainerDao.save(entity);

        log.info(
                "Trainer created successfully id={} username={}",
                savedTrainer.getUserId(),
                savedTrainer.getUsername()
        );

        return savedTrainer;
    }

    @Override
    public Trainer updateTrainer(Trainer entity) {
        log.info("Updating trainer id={}", entity.getUserId());

        Trainer updatedTrainer = trainerDao.save(entity);

        log.info("Trainer updated successfully id={}", updatedTrainer.getUserId());

        return updatedTrainer;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }
}