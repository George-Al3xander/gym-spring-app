package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.model.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfig.class)
@Transactional
class TraineeDaoTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TraineeDao traineeDao;

    @Test
    void givenTraineeWithoutId_whenSave_thenTraineeIsStoredWithGeneratedId() {
        Trainee saved = traineeDao.save(generateTrainee());

        assertNotNull(saved.getId());
        assertTrue(traineeDao.findById(saved.getId()).isPresent());
    }

    @Test
    void givenExistingTrainee_whenFindById_thenReturnOptionalOfTrainee() {
        Trainee trainee = generateTrainee();

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
        Trainee t1 = generateTrainee();
        Trainee t2 = generateTrainee();

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
        Trainee trainee = generateTrainee();

        entityManager.persist(trainee);
        entityManager.flush();

        traineeDao.delete(trainee.getId());

        assertTrue(traineeDao.findById(trainee.getId()).isEmpty());
    }

    @Test
    void givenExistingTrainee_whenUpdate_thenReturnUpdatedTrainee() {
        Trainee trainee = generateTrainee();

        entityManager.persist(trainee);
        entityManager.flush();

        trainee.setFirstName("Updated");

        Trainee result = traineeDao.update(trainee);

        assertEquals("Updated", result.getFirstName());
        assertEquals(trainee.getId(), result.getId());
    }

    private Trainee generateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john.doe");
        trainee.setPassword("pass");
        trainee.setIsActive(true);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("Kyiv");
        return trainee;
    }
}