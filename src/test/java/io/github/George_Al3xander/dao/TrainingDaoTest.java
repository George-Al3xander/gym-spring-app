package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Training;
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
class TrainingDaoTest {

    @Mock
    private Storage storage;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @Mock
    private Map<Long, Training> trainingStorage;

    @InjectMocks
    private TrainingDao trainingDao;

    private void init() {
        trainingStorage = new HashMap<>();
        when(storage.getTrainingStorage()).thenReturn(trainingStorage);
    }

    @Test
    void givenTraining_whenSave_thenStoredWithGeneratedKey() {
        init();
        long nextId = 1L;
        when(sequenceGenerator.getNextSeq()).thenReturn(nextId);

        Training training = new Training();

        trainingDao.save(training);
        assertNotNull(trainingStorage.get(nextId));
    }

    @Test
    void givenExistingTraining_whenFindById_thenReturnTraining() {
        init();

        long id = 123L;
        Training training = new Training();

        trainingStorage.put(id, training);

        Optional<Training> result = trainingDao.findById(id);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void givenMissingTraining_whenFindById_thenReturnEmpty() {
        init();

        Optional<Training> result = trainingDao.findById(-1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainings_whenFindAll_thenReturnAll() {
        init();

        Training t1 = new Training();
        Training t2 = new Training();

        trainingStorage.put(1L, t1);
        trainingStorage.put(2L, t2);

        List<Training> result = trainingDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));

        assertThrows(UnsupportedOperationException.class,
                () -> result.add(new Training()));
    }

    @Test
    void givenTraining_whenDelete_thenRemovedFromStorage() {
        init();

        long id = 123L;

        trainingDao.delete(id);

        assertNull(trainingStorage.get(id));
    }

    @Test
    void givenExistingTraining_whenUpdate_thenReplaceExistingEntry() {
        init();

        Training training = new Training();

        trainingStorage.put(13L, training);

        Training result = trainingDao.update(training);

        assertEquals(training, result);
        assertNotNull(trainingStorage.get(13L));
    }

    @Test
    void givenTrainingNotInStorage_whenUpdate_thenThrowException() {
        init();

        Training training = new Training();

        assertThrows(IllegalArgumentException.class,
                () -> trainingDao.update(training));

    }
}