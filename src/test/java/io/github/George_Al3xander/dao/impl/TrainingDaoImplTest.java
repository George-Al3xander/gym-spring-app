package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfig.class)
@Transactional
class TrainingDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TrainingDao trainingDao;


    @Test
    void givenTraining_whenSave_thenStoredWithGeneratedKey() {
        Training training = trainingDao.save(generateTraining());

        assertNotNull(training.getId());
        assertTrue(trainingDao.findById(training.getId()).isPresent());
    }

    @Test
    void givenExistingTraining_whenFindById_thenReturnTraining() {
        Training training = generateTraining();

        entityManager.persist(training);
        entityManager.flush();

        Optional<Training> result = trainingDao.findById(training.getId());

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void givenMissingTraining_whenFindById_thenReturnEmpty() {
        Optional<Training> result = trainingDao.findById(-1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainings_whenFindAll_thenReturnAll() {
        Training t1 = generateTraining();
        Training t2 = generateTraining();

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.flush();

        List<Training> result = trainingDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void givenTraining_whenDelete_thenRemovedFromStorage() {
        Training training = generateTraining();

        entityManager.persist(training);
        entityManager.flush();

        trainingDao.delete(training.getId());

        assertTrue(trainingDao.findById(training.getId()).isEmpty());
    }

    @Test
    void givenExistingTraining_whenUpdate_thenReplaceExistingEntry() {
        Training training = generateTraining();

        entityManager.persist(training);
        entityManager.flush();

        Training result = trainingDao.update(training);

        assertEquals(training, result);
    }

    private Training generateTraining() {
        Training training = new Training();

        training.setTrainingDate(LocalDateTime.of(2024, 1, 1, 3, 1));
        training.setTrainingName("Strength Training");
        training.setDurationSeconds(60);

        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("john.trainer");
        trainer.setPassword("pass");
        trainer.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setFirstName("Jane");
        trainee.setLastName("Smith");
        trainee.setUsername("jane.trainee");
        trainee.setPassword("pass");
        trainee.setIsActive(true);
        trainee.setDateOfBirth(LocalDate.of(1995, 5, 5));
        trainee.setAddress("Kyiv");

        entityManager.persist(trainer);
        entityManager.persist(trainee);
        entityManager.flush();

        training.setTrainer(trainer);
        training.setTrainee(trainee);

        return training;
    }
}