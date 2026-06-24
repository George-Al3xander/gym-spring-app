package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDaoTest {

    @Mock
    private Storage storage;

    @Mock
    private Map<String, Training> trainingStorage;

    @InjectMocks
    private TrainingDao trainingDao;

    private void init() {
        when(storage.getTrainingStorage()).thenReturn(trainingStorage);
    }

    @Test
    void givenTraining_whenSave_thenStoredWithGeneratedKey() {
        init();

        Training training = new Training();

        trainingDao.save(training);

        verify(trainingStorage, times(1))
                .put(anyString(), eq(training));
    }

    @Test
    void givenExistingTraining_whenFindById_thenReturnTraining() {
        init();

        String id = "123";
        Training training = new Training();

        when(trainingStorage.get(id)).thenReturn(training);

        Optional<Training> result = trainingDao.findById(id);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void givenMissingTraining_whenFindById_thenReturnEmpty() {
        init();

        when(trainingStorage.get("missing")).thenReturn(null);

        Optional<Training> result = trainingDao.findById("missing");

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainings_whenFindAll_thenReturnAll() {
        init();

        Training t1 = new Training();
        Training t2 = new Training();

        when(trainingStorage.values()).thenReturn(List.of(t1, t2));

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

        String id = "123";

        trainingDao.delete(id);

        verify(trainingStorage, times(1)).remove(id);
    }

    @Test
    void givenExistingTraining_whenUpdate_thenReplaceExistingEntry() {
        init();

        Training training = new Training();

        Map.Entry<String, Training> entry =
                Map.entry("key-1", training);

        when(trainingStorage.entrySet())
                .thenReturn(Set.of(entry));

        when(trainingStorage.put("key-1", training))
                .thenReturn(training);

        Training result = trainingDao.update(training);

        assertEquals(training, result);

        verify(trainingStorage).put("key-1", training);
    }

    @Test
    void givenTrainingNotInStorage_whenUpdate_thenThrowException() {
        init();

        Training training = new Training();

        when(trainingStorage.entrySet())
                .thenReturn(Collections.emptySet());

        assertThrows(IllegalArgumentException.class,
                () -> trainingDao.update(training));

        verify(trainingStorage, never()).put(any(), any());
    }
}