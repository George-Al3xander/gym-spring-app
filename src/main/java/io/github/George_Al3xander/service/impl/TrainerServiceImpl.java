package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.UsernameGenerator;
import io.github.George_Al3xander.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    private UsernameGenerator usernameGenerator;

    @Override
    public Trainer getTrainerById(String id) {

        Optional<Trainer> optionalTrainer = trainerDao.findById(id);

        if (optionalTrainer.isEmpty()) {
            throw new EntityNotFoundException("Trainer", id);
        }

        return optionalTrainer.get();
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDao.findAll();
    }

    @Override
    public Trainer saveTrainer(Trainer entity) {
        entity.setUsername(usernameGenerator.generateUsername(entity));
        entity.setPassword(PasswordGenerator.generatePassword(10));

        return trainerDao.save(entity);
    }

    @Override
    public Trainer updateTrainer(Trainer entity) {
        return trainerDao.save(entity);
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }
}
