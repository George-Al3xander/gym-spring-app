package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.storage.Storage;
import io.github.George_Al3xander.util.SequenceGenerator;
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
    private SequenceGenerator sequenceGenerator;

    @Mock
    private Map<Long, Trainee> traineeStorage;

    @InjectMocks
    private TraineeDao traineeDao;

    @BeforeEach
    void setUp() {
        when(storage.getTraineeStorage()).thenReturn(traineeStorage);

    }

    @Test
    void givenTraineeWithoutId_whenSave_thenTraineeIsStoredWithGeneratedId() {
        when(sequenceGenerator.getNextSeq()).thenReturn(1L);
        Trainee trainee = new Trainee();

        Trainee saved = traineeDao.save(trainee);

        assertNotNull(saved.getUserId());

        verify(traineeStorage, times(1))
                .put(eq(saved.getUserId()), eq(saved));
    }

    @Test
    void givenExistingTrainee_whenFindById_thenReturnOptionalOfTrainee() {
        long id = 123L;
        Trainee trainee = new Trainee();
        when(traineeStorage.get(id)).thenReturn(trainee);

        Optional<Trainee> result = traineeDao.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void givenNonExistingTrainee_whenFindById_thenReturnEmptyOptional() {
        when(traineeStorage.get(0L)).thenReturn(null);

        Optional<Trainee> result = traineeDao.findById(0L);

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
        long id = 123L;

        traineeDao.delete(id);

        verify(traineeStorage, times(1)).remove(id);
    }

    @Test
    void givenExistingTrainee_whenUpdate_thenReturnUpdatedTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(123L);

        when(traineeStorage.containsKey(123L)).thenReturn(true);
        when(traineeStorage.put(123L, trainee)).thenReturn(trainee);

        Trainee result = traineeDao.update(trainee);

        assertEquals(trainee, result);

        verify(traineeStorage).put(123L, trainee);
    }

    @Test
    void givenNonExistingTrainee_whenUpdate_thenThrowEntityNotFoundException() {
        Trainee trainee = new Trainee();
        trainee.setUserId(123L);

        when(traineeStorage.containsKey(123L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> traineeDao.update(trainee));

        verify(traineeStorage, never()).put(any(), any());
    }
}