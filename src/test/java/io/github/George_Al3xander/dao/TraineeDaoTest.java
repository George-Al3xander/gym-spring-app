package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDaoTest {

    @Mock
    private Storage storage;

    @Mock
    private Map<String, Trainee> traineeStorage;

    @InjectMocks
    private TraineeDao traineeDao;

    @BeforeEach
    void setUp() {
        when(storage.getTraineeStorage()).thenReturn(traineeStorage);
    }

    @Test
    void givenTraineeWithoutId_whenSave_thenTraineeIsStoredWithGeneratedId() {
        Trainee trainee = new Trainee();

        Trainee saved = traineeDao.save(trainee);

        assertNotNull(saved.getUserId());

        verify(traineeStorage, times(1))
                .put(eq(saved.getUserId()), eq(saved));
    }

    @Test
    void givenExistingTrainee_whenFindById_thenReturnOptionalOfTrainee() {
        String id = "123";
        Trainee trainee = new Trainee();
        when(traineeStorage.get(id)).thenReturn(trainee);

        Optional<Trainee> result = traineeDao.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void givenNonExistingTrainee_whenFindById_thenReturnEmptyOptional() {
        when(traineeStorage.get("missing")).thenReturn(null);

        Optional<Trainee> result = traineeDao.findById("missing");

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleTrainees_whenFindAll_thenReturnAllTrainees() {
        Trainee t1 = new Trainee();
        Trainee t2 = new Trainee();

        when(traineeStorage.values()).thenReturn(List.of(t1, t2));

        List<Trainee> result = traineeDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));

        assertThrows(UnsupportedOperationException.class,
                () -> result.add(new Trainee()));
    }

    @Test
    void givenExistingTrainee_whenDelete_thenRemoveFromStorage() {
        String id = "123";

        traineeDao.delete(id);

        verify(traineeStorage, times(1)).remove(id);
    }

    @Test
    void givenExistingTrainee_whenUpdate_thenReturnUpdatedTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId("123");

        when(traineeStorage.containsKey("123")).thenReturn(true);
        when(traineeStorage.put("123", trainee)).thenReturn(trainee);

        Trainee result = traineeDao.update(trainee);

        assertEquals(trainee, result);

        verify(traineeStorage).put("123", trainee);
    }

    @Test
    void givenNonExistingTrainee_whenUpdate_thenThrowEntityNotFoundException() {
        Trainee trainee = new Trainee();
        trainee.setUserId("missing");

        when(traineeStorage.containsKey("missing")).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> traineeDao.update(trainee));

        verify(traineeStorage, never()).put(any(), any());
    }
}