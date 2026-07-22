package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
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
        filter.setTrainingTypeId(training.getTrainingType().getId());

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
                .trainingTypeId(training.getTrainingType().getId())
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

    @Test
    void givenMatchingTrainerUsernames_whenDeleteForTrainee_thenDeleteMatchingTrainings() {
        Training t1 = persistTraining("trainer1", "trainee");
        Training t2 = persistTraining("trainer2", "trainee");
        Training t3 = persistTraining("trainer3", "trainee");

        int deleted = trainingDao.deleteForTraineeByTrainerUsernames(
                "trainee",
                List.of("trainer1", "trainer3")
        );

        entityManager.flush();
        entityManager.clear();

        assertEquals(2, deleted);
        assertTrue(trainingDao.findById(t1.getId()).isEmpty());
        assertFalse(trainingDao.findById(t2.getId()).isEmpty());
        assertTrue(trainingDao.findById(t3.getId()).isEmpty());
    }

    @Test
    void givenUnknownTrainerUsernames_whenDeleteForTrainee_thenDeleteNothing() {
        Training training = persistTraining("trainer1", "trainee");

        int deleted = trainingDao.deleteForTraineeByTrainerUsernames(
                "trainee",
                List.of("unknown")
        );

        entityManager.flush();
        entityManager.clear();

        assertEquals(0, deleted);
        assertTrue(trainingDao.findById(training.getId()).isPresent());
    }

    @Test
    void givenAnotherTraineeWithSameTrainer_whenDeleteForTrainee_thenOnlySpecifiedTraineeDeleted() {
        Training trainee1Training = persistTraining("trainer1", "trainee1");
        Training trainee2Training = persistTraining("trainer1", "trainee2");

        int deleted = trainingDao.deleteForTraineeByTrainerUsernames(
                "trainee1",
                List.of("trainer1")
        );

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, deleted);
        assertTrue(trainingDao.findById(trainee1Training.getId()).isEmpty());
        assertTrue(trainingDao.findById(trainee2Training.getId()).isPresent());
    }

    private Training persistTraining(
            String trainerUsername,
            String traineeUsername) {

        Training training = generateTraining(trainerUsername, traineeUsername);

        Trainer trainer = findTrainer(trainerUsername);
        if (trainer != null) {
            training.setTrainer(trainer);
        } else {
            entityManager.persist(training.getTrainer());
        }

        Trainee trainee = findTrainee(traineeUsername);
        if (trainee != null) {
            training.setTrainee(trainee);
        } else {
            entityManager.persist(training.getTrainee());
        }

        entityManager.persist(training);
        entityManager.flush();
        return training;
    }

    private Trainer findTrainer(String username) {
        return entityManager.createQuery(
                        "from Trainer t where t.username = :u", Trainer.class)
                .setParameter("u", username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private Trainee findTrainee(String username) {
        return entityManager.createQuery(
                        "from Trainee t where t.username = :username", Trainee.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);
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