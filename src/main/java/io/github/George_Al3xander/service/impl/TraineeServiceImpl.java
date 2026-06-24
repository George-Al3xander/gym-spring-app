package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityInUseException;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.UsernameGenerator;
import io.github.George_Al3xander.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingDao trainingDao;

    private UsernameGenerator usernameGenerator;

    @Override
    public Trainee getTraineeById(String id) {

        Optional<Trainee> optionalTrainee = traineeDao.findById(id);

        if (optionalTrainee.isEmpty()) {
            throw new EntityNotFoundException("Trainee", id);
        }

        return optionalTrainee.get();
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDao.findAll();
    }

    @Override
    public Trainee saveTrainee(Trainee entity) {
        entity.setUsername(usernameGenerator.generateUsername(entity));
        entity.setPassword(PasswordGenerator.generatePassword(10));

        return traineeDao.save(entity);
    }

    @Override
    public Trainee updateTrainee(Trainee entity) {
        return traineeDao.save(entity);
    }

    @Override
    public void deleteTrainee(String id) {

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
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }
}
