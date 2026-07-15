package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class TraineeDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TraineeDaoImpl traineeDao;

    @Test
    void givenTraineeWithoutId_whenSave_thenTraineeIsStoredWithGeneratedId() {
        Trainee saved = traineeDao.save(createTrainee());

        assertNotNull(saved.getId());
        assertTrue(traineeDao.findById(saved.getId()).isPresent());
    }

    @Test
    void givenExistingTrainee_whenFindById_thenReturnOptionalOfTrainee() {
        Trainee trainee = createTrainee();

        entityManager.persist(trainee);
        entityManager.flush();

        Optional<Trainee> result = traineeDao.findById(trainee.getId());

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void givenNonExistingTrainee_whenFindById_thenReturnEmptyOptional() {
        Optional<Trainee> result = traineeDao.findById(-1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainees_whenFindAll_thenReturnAllTrainees() {
        Trainee t1 = createTrainee();
        Trainee t2 = createTrainee();

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.flush();

        List<Trainee> result = traineeDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void givenExistingTrainee_whenDelete_thenRemoveFromStorage() {
        Trainee trainee = createTrainee();

        entityManager.persist(trainee);
        entityManager.flush();

        traineeDao.delete(trainee.getId());

        assertTrue(traineeDao.findById(trainee.getId()).isEmpty());
    }

    @Test
    void givenExistingTrainee_whenUpdate_thenReturnUpdatedTrainee() {
        Trainee trainee = createTrainee();

        entityManager.persist(trainee);
        entityManager.flush();

        trainee.setFirstName("Updated");

        Trainee result = traineeDao.update(trainee);

        assertEquals("Updated", result.getFirstName());
        assertEquals(trainee.getId(), result.getId());
    }

    @Test
    void givenExistingTrainee_whenFindByUsername_thenReturnOptionalOfTrainee() {
        Trainee trainee = createTrainee();

        entityManager.persist(trainee);
        entityManager.flush();

        Optional<Trainee> result = traineeDao.findByUsername(trainee.getUsername());

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void givenNonExistingUsername_whenFindByUsername_thenReturnEmptyOptional() {
        assertThrows(NoResultException.class, () -> traineeDao.findByUsername("non-existing-username"));
    }

    @Test
    void givenAssignedTrainees_whenFindAllByTrainerUsernameWithAssignedTrue_thenReturnAssignedTrainees() {
        Trainer trainer = createTrainer();

        Trainee assigned = createTrainee();
        Trainee notAssigned = createTrainee();

        entityManager.persist(trainer);
        entityManager.persist(assigned);
        entityManager.persist(notAssigned);

        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(assigned);
        training.setTrainingName("Strength");
        training.setTrainingDate(LocalDateTime.now());
        training.setDurationSeconds(60);

        entityManager.persist(training);
        entityManager.flush();

        List<Trainee> result = traineeDao.findAllByTrainerUsername(trainer.getUsername(), true);

        assertEquals(1, result.size());
        assertTrue(result.contains(assigned));
        assertFalse(result.contains(notAssigned));
    }

    @Test
    void givenAssignedTrainees_whenFindAllByTrainerUsernameWithAssignedFalse_thenReturnUnassignedTrainees() {
        Trainer trainer = createTrainer();

        Trainee assigned = createTrainee();
        Trainee unassigned = createTrainee();

        entityManager.persist(trainer);
        entityManager.persist(assigned);
        entityManager.persist(unassigned);

        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(assigned);
        training.setTrainingName("Strength");
        training.setTrainingDate(LocalDateTime.now());
        training.setDurationSeconds(60);

        entityManager.persist(training);
        entityManager.flush();

        List<Trainee> result = traineeDao.findAllByTrainerUsername(trainer.getUsername(), false);

        assertEquals(1, result.size());
        assertTrue(result.contains(unassigned));
        assertFalse(result.contains(assigned));
    }

    private Trainee createTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername(UUID.randomUUID().toString());
        trainee.setPassword("1234567890");
        trainee.setIsActive(true);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("Kyiv");
        return trainee;
    }

    private Trainer createTrainer() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Fitness");
        entityManager.persist(trainingType);

        Trainer trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        trainer.setUsername(UUID.randomUUID().toString());
        trainer.setPassword("1234567890");
        trainer.setIsActive(true);
        trainer.setSpecialization(trainingType);
        return trainer;
    }
}