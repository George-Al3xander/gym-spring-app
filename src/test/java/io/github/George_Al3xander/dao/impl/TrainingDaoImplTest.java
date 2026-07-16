package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.model.TrainingType;
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
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class TrainingDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TrainingDao trainingDao;

    @Test
    void givenTraining_whenSave_thenStoredWithGeneratedKey() {
        Training training = trainingDao.save(persistTraining(
                "john.trainer",
                "jane.trainee"));

        assertNotNull(training.getId());
        assertTrue(trainingDao.findById(training.getId()).isPresent());
    }

    @Test
    void givenExistingTraining_whenFindById_thenReturnTraining() {
        Training training = persistTraining(
                "john.trainer",
                "jane.trainee");
        ;

        Optional<Training> result = trainingDao.findById(training.getId());

        assertTrue(result.isPresent());
        assertEquals(training.getId(), result.get().getId());
    }

    @Test
    void givenMissingTraining_whenFindById_thenReturnEmpty() {
        assertTrue(trainingDao.findById(-1L).isEmpty());
    }

    @Test
    void givenMultipleTrainings_whenFindAll_thenReturnAll() {
        Training t1 = persistTraining(
                "john.trainer1",
                "jane.trainee1");
        Training t2 = persistTraining(
                "john.trainer2",
                "jane.trainee2");

        List<Training> result = trainingDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void givenTraining_whenDelete_thenRemovedFromStorage() {
        Training training = persistTraining(
                "john.trainer1",
                "jane.trainee1");

        trainingDao.delete(training.getId());

        assertTrue(trainingDao.findById(training.getId()).isEmpty());
    }

    @Test
    void givenTraining_whenUpdate_thenReplaceExistingEntry() {
        Training training = persistTraining(
                "john.trainer1",
                "jane.trainee1");

        Training result = trainingDao.update(training);

        assertEquals(training.getId(), result.getId());
    }

    @Test
    void givenTrainings_whenFindByTraineeUsername_withoutFilter_thenReturnAllForTrainee() {
        Training training = persistTraining(
                "john.trainer1",
                "jane.trainee");

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", null);

        assertEquals(1, result.size());
        assertEquals(training.getId(), result.get(0).getId());
    }

    @Test
    void givenFromDateOnly_whenFilter_thenReturnMatching() {
        persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = TrainingFilter.builder()
                .fromDate(LocalDateTime.of(2023, 12, 31, 0, 0))
                .build();

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertFalse(result.isEmpty());
    }

    @Test
    void givenToDateOnly_whenFilter_thenReturnMatching() {
        persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = TrainingFilter.builder()
                .toDate(LocalDateTime.of(2024, 1, 2, 0, 0))
                .build();

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertFalse(result.isEmpty());
    }

    @Test
    void givenTraineeFirstName_whenFilter_thenReturnMatching() {
        persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = new TrainingFilter();
        filter.setTraineeFirstName("Jane");

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertFalse(result.isEmpty());
    }

    @Test
    void givenTraineeLastName_whenFilter_thenReturnMatching() {
        persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = new TrainingFilter();
        filter.setTraineeLastName("Smith");

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertFalse(result.isEmpty());
    }

    @Test
    void givenTrainerFirstName_whenFilter_thenReturnMatching() {
        persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = new TrainingFilter();
        filter.setTrainerFirstName("John");

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertFalse(result.isEmpty());
    }

    @Test
    void givenTrainerLastName_whenFilter_thenReturnMatching() {
        persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = new TrainingFilter();
        filter.setTrainerLastName("Doe");

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertFalse(result.isEmpty());
    }

    @Test
    void givenTrainingType_whenFilter_thenReturnMatching() {
        Training training = persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = new TrainingFilter();
        filter.setTrainingType(training.getTrainingType());

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertEquals(1, result.size());
    }

    @Test
    void givenFullFilter_whenApplied_thenReturnMatching() {
        Training training = persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = TrainingFilter.builder()
                .fromDate(LocalDateTime.of(2023, 12, 31, 0, 0))
                .toDate(LocalDateTime.of(2024, 1, 2, 0, 0))
                .traineeFirstName("Jane")
                .traineeLastName("Smith")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .trainingType(training.getTrainingType())
                .build();

        List<Training> result =
                trainingDao.findByTraineeUsername("jane.trainee", filter);

        assertEquals(1, result.size());
    }

    @Test
    void givenTrainerUsername_whenNoFilter_thenReturnAll() {
        Training training = persistTraining(
                "john.trainer",
                "jane.trainee");

        List<Training> result =
                trainingDao.findByTrainerUsername("john.trainer", null);

        assertEquals(1, result.size());
        assertEquals(training.getId(), result.get(0).getId());
    }


    @Test
    void givenTrainerFullFilter_whenApplied_thenReturnMatching() {
        Training training = persistTraining(
                "john.trainer",
                "jane.trainee");

        TrainingFilter filter = TrainingFilter.builder()
                .fromDate(LocalDateTime.of(2023, 12, 31, 0, 0))
                .toDate(LocalDateTime.of(2024, 1, 2, 0, 0))
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .build();

        List<Training> result =
                trainingDao.findByTrainerUsername("john.trainer", filter);

        assertEquals(1, result.size());
    }

    private Training persistTraining(String trainerUsername, String traineeUsername) {
        Training training = generateTraining(trainerUsername, traineeUsername);

        entityManager.persist(training.getTrainer());
        entityManager.persist(training.getTrainee());
        entityManager.persist(training);

        entityManager.flush();

        return training;
    }

    private Training generateTraining(String trainerUsername, String traineeUsername) {
        Training training = new Training();

        training.setTrainingDate(LocalDateTime.of(2024, 1, 1, 3, 1));
        training.setTrainingName("Strength Training");
        training.setDurationSeconds(60);

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("CARDIO");
        entityManager.persist(trainingType);

        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername(trainerUsername);
        trainer.setPassword("1234567890");
        trainer.setIsActive(true);
        trainer.setSpecialization(trainingType);

        Trainee trainee = new Trainee();
        trainee.setFirstName("Jane");
        trainee.setLastName("Smith");
        trainee.setUsername(traineeUsername);
        trainee.setPassword("1234567890");
        trainee.setIsActive(true);
        trainee.setDateOfBirth(LocalDate.of(1995, 5, 5));
        trainee.setAddress("Kyiv");

        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);

        return training;
    }
}