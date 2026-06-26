package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.storage.Storage;
import io.github.George_Al3xander.util.SequenceGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDaoTest {

    @Mock
    private Storage storage;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @Mock
    private Map<Long, Trainer> trainerStorage;

    @InjectMocks
    private TrainerDao trainerDao;

    private void initStorageMock() {
        trainerStorage = new HashMap<>();
        when(storage.getTrainerStorage()).thenReturn(trainerStorage);
    }

    @Test
    void givenTrainerWithoutId_whenSave_thenTrainerIsStoredWithGeneratedId() {
        initStorageMock();
        when(sequenceGenerator.getNextSeq()).thenReturn(1L);
        Trainer trainer = new Trainer();
        Long id = trainer.getUserId();

        Trainer saved = trainerDao.save(trainer);

        assertNotNull(saved.getUserId());
        assertNotEquals(id, saved.getUserId());
    }

    @Test
    void givenExistingTrainer_whenFindById_thenReturnTrainer() {
        initStorageMock();

        long id = 123L;
        Trainer trainer = new Trainer();
        trainer.setUserId(id);

        trainerStorage.put(id, trainer);

        Optional<Trainer> result = trainerDao.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void givenMissingTrainer_whenFindById_thenReturnEmpty() {
        initStorageMock();

        Optional<Trainer> result = trainerDao.findById(0L);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainers_whenFindAll_thenReturnAllTrainers() {
        initStorageMock();

        Trainer t1 = new Trainer();
        Trainer t2 = new Trainer();

        trainerStorage.put(1L, t1);
        trainerStorage.put(2L, t2);

        List<Trainer> result = trainerDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));

        assertThrows(UnsupportedOperationException.class,
                () -> result.add(new Trainer()));
    }

    @Test
    void givenTrainerExists_whenDelete_thenRemoveFromStorage() {
        initStorageMock();

        long id = 123L;

        trainerDao.delete(id);

        assertNull(trainerStorage.get(id));
    }

    @Test
    void givenExistingTrainer_whenUpdate_thenReturnUpdatedTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUserId(123L);

        Map<Long, Trainer> storage = new HashMap<>();
        storage.put(123L, new Trainer());

        when(this.storage.getTrainerStorage()).thenReturn(storage);

        Trainer result = trainerDao.update(trainer);

        assertEquals(trainer, result);
        assertSame(trainer, storage.get(123L));
    }

    @Test
    void givenMissingTrainer_whenUpdate_thenThrowEntityNotFoundException() {
        initStorageMock();

        Trainer trainer = new Trainer();
        trainer.setUserId(0L);

        assertThrows(EntityNotFoundException.class,
                () -> trainerDao.update(trainer));

    }
}