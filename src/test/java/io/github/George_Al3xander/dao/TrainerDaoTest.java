package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfig.class)
@Transactional
class TrainerDaoTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TrainerDao trainerDao;

    @Test
    void givenTrainerWithoutId_whenSave_thenTrainerIsStoredWithGeneratedId() {
        Trainer trainer = trainerDao.save(generateTrainer());

        Long id = trainer.getId();

        assertNotNull(trainerDao.findById(id));
    }

    @Test
    void givenExistingTrainer_whenFindById_thenReturnTrainer() {
        Trainer trainer = generateTrainer();

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
        Trainer t1 = generateTrainer();
        Trainer t2 = generateTrainer();

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
        long id = 123L;

        trainerDao.delete(id);

        assertTrue(trainerDao.findById(id).isEmpty());
    }

    @Test
    void givenExistingTrainer_whenUpdate_thenReturnUpdatedTrainer() {
        Trainer trainer = generateTrainer();
        entityManager.persist(trainer);
        entityManager.flush();

        Trainer result = trainerDao.update(trainer);

        assertEquals(trainer, result);
    }
    
    private static Trainer generateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("john.doe");
        trainer.setPassword("1234");
        trainer.setIsActive(true);

        return trainer;
    }
}