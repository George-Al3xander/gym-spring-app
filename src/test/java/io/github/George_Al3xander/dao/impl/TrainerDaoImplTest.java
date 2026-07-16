package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
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
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class TrainerDaoImplTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TrainerDaoImpl trainerDao;

    private static TrainingType trainingType;

    @BeforeEach
    public void setup() {
        trainingType = new TrainingType(null, "Training");
        entityManager.persist(trainingType);
        entityManager.flush();
    }

    @Test
    void givenTrainerWithoutId_whenSave_thenTrainerIsStoredWithGeneratedId() {
        Trainer trainer = trainerDao.save(generateTrainer("john.doe"));

        Long id = trainer.getId();

        assertNotNull(trainerDao.findById(id));
    }

    @Test
    void givenExistingTrainer_whenFindById_thenReturnTrainer() {
        Trainer trainer = generateTrainer("john.doe");

        entityManager.persist(trainer);
        entityManager.flush();

        Optional<Trainer> result = trainerDao.findById(trainer.getId());

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void givenMissingTrainer_whenFindById_thenReturnEmpty() {
        Optional<Trainer> result = trainerDao.findById(0L);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainers_whenFindAll_thenReturnAllTrainers() {
        Trainer t1 = generateTrainer("john.doe");
        Trainer t2 = generateTrainer("john.doe1");

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.flush();

        List<Trainer> result = trainerDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void givenTrainerExists_whenDelete_thenRemoveFromStorage() {
        Trainer trainer = generateTrainer("john.doe3");

        entityManager.persist(trainer);
        entityManager.flush();

        long id = trainer.getId();

        trainerDao.delete(id);

        assertTrue(trainerDao.findById(id).isEmpty());
    }

    @Test
    void givenExistingTrainer_whenUpdate_thenReturnUpdatedTrainer() {
        Trainer trainer = generateTrainer("john.doe");
        entityManager.persist(trainer);
        entityManager.flush();

        Trainer result = trainerDao.update(trainer);

        assertEquals(trainer, result);
    }

    @Test
    void givenExistingTrainer_whenFindByUsername_thenReturnTrainer() {
        Trainer trainer = generateTrainer("john.doe");

        entityManager.persist(trainer);
        entityManager.flush();

        Optional<Trainer> result = trainerDao.findByUsername("john.doe");

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void givenMissingUsername_whenFindByUsername_thenReturnEmpty() {
        assertThrows(NoResultException.class, () -> trainerDao.findByUsername("missing.username"));
    }

    @Test
    void givenTraineeUsername_whenFindByTraineeUsernameWithAssignedFalse_thenReturnUnassignedTrainers() {
        Trainer t1 = generateTrainer("trainer.one");
        Trainer t2 = generateTrainer("trainer.two");

        Trainee trainee1 = generateTrainee("some.trainee1");
        Trainee trainee2 = generateTrainee("some.trainee2");

        entityManager.persist(trainee1);
        entityManager.persist(trainee2);
        entityManager.persist(t1);
        entityManager.persist(t2);

        entityManager.persist(generateTraining(t1, trainee2));
        entityManager.persist(generateTraining(t2, trainee2));

        entityManager.flush();

        List<Trainer> result =
                trainerDao.findAllByTraineeUsername(trainee1.getUsername(), new TrainerFilter(true, false));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void givenAllTrainersAssignedToTrainee_whenFindByTraineeUsernameWithAssignedFalse_thenReturnEmptyList() {
        Trainer trainer = generateTrainer("trainer.one");
        Trainee trainee = generateTrainee("john.doe");

        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.persist(generateTraining(trainer, trainee));

        entityManager.flush();

        List<Trainer> result =
                trainerDao.findAllByTraineeUsername("john.doe", new TrainerFilter(true, false));

        assertTrue(result.isEmpty());
    }

    @Test
    void givenAssignedTrainer_whenFindByTraineeUsernameWithAssignedTrue_thenReturnAssignedTrainers() {
        Trainer assignedTrainer = generateTrainer("trainer.one");
        Trainer unassignedTrainer = generateTrainer("trainer.two");
        Trainee trainee = generateTrainee("john.doe");

        entityManager.persist(trainee);
        entityManager.persist(assignedTrainer);
        entityManager.persist(unassignedTrainer);

        entityManager.persist(generateTraining(assignedTrainer, trainee));

        entityManager.flush();

        List<Trainer> result =
                trainerDao.findAllByTraineeUsername("john.doe", new TrainerFilter(true, true));

        assertEquals(1, result.size());
        assertTrue(result.contains(assignedTrainer));
        assertFalse(result.contains(unassignedTrainer));
    }

    @Test
    void givenInactiveTrainer_whenFindByTraineeUsername_thenInactiveTrainerIsNotReturned() {
        Trainer activeTrainer = generateTrainer("trainer.active");
        Trainer inactiveTrainer = generateTrainer("trainer.inactive");
        inactiveTrainer.setIsActive(false);

        Trainee trainee = generateTrainee("john.doe");

        entityManager.persist(trainee);
        entityManager.persist(activeTrainer);
        entityManager.persist(inactiveTrainer);

        entityManager.flush();

        List<Trainer> result = trainerDao.findAllByTraineeUsername(
                trainee.getUsername(),
                new TrainerFilter(true, false)
        );

        assertEquals(1, result.size());
        assertTrue(result.contains(activeTrainer));
        assertFalse(result.contains(inactiveTrainer));
    }

    @Test
    void givenAssignedInactiveTrainer_whenAssignedTrue_thenOnlyActiveAssignedTrainerReturned() {
        Trainer activeTrainer = generateTrainer("trainer.active");
        Trainer inactiveTrainer = generateTrainer("trainer.inactive");
        inactiveTrainer.setIsActive(false);

        Trainee trainee = generateTrainee("john.doe");

        entityManager.persist(trainee);
        entityManager.persist(activeTrainer);
        entityManager.persist(inactiveTrainer);

        entityManager.persist(generateTraining(activeTrainer, trainee));
        entityManager.persist(generateTraining(inactiveTrainer, trainee));

        entityManager.flush();

        List<Trainer> result = trainerDao.findAllByTraineeUsername(
                trainee.getUsername(),
                new TrainerFilter(true, true)
        );

        assertEquals(1, result.size());
        assertTrue(result.contains(activeTrainer));
        assertFalse(result.contains(inactiveTrainer));
    }

    private static Trainer generateTrainer(String username) {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername(username);
        trainer.setPassword("1234567890");
        trainer.setSpecialization(trainingType);
        trainer.setIsActive(true);

        return trainer;
    }

    private static Trainee generateTrainee(String username) {
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setAddress("address");
        trainee.setPassword("1234567890");
        trainee.setFirstName("Name");
        trainee.setLastName("Name");
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setIsActive(true);

        return trainee;
    }

    private static Training generateTraining(Trainer trainer, Trainee trainee) {
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setDurationSeconds(123);
        training.setTrainingDate(LocalDateTime.now());
        trainer.setSpecialization(trainingType);
        training.setTrainingName("Training");

        return training;
    }
}