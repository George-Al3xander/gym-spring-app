package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.model.TrainingType;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training(
                "trainee-1",
                "trainer-1",
                "Morning cardio",
                TrainingType.CARDIO,
                LocalDateTime.of(2026, 1, 10, 8, 0),
                3600
        );
    }

    @Test
    void givenExistingTraining_whenGetTrainingById_thenReturnTraining() {

        when(trainingDao.findById("training-1"))
                .thenReturn(Optional.of(training));

        Training result =
                trainingService.getTrainingById("training-1");

        assertEquals(training, result);

        verify(trainingDao)
                .findById("training-1");
    }


    @Test
    void givenNonExistingTraining_whenGetTrainingById_thenThrowEntityNotFoundException() {

        when(trainingDao.findById("training-1"))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> trainingService.getTrainingById("training-1")
        );

        verify(trainingDao)
                .findById("training-1");
    }


    @Test
    void givenExistingTrainings_whenGetAllTrainings_thenReturnTrainingList() {

        List<Training> trainings = List.of(training);

        when(trainingDao.findAll())
                .thenReturn(trainings);

        List<Training> result =
                trainingService.getAllTrainings();

        assertEquals(1, result.size());
        assertEquals(training, result.get(0));

        verify(trainingDao)
                .findAll();
    }


    @Test
    void givenNoTrainings_whenGetAllTrainings_thenReturnEmptyList() {

        when(trainingDao.findAll())
                .thenReturn(Collections.emptyList());

        List<Training> result =
                trainingService.getAllTrainings();

        assertTrue(result.isEmpty());

        verify(trainingDao)
                .findAll();
    }


    @Test
    void givenValidTraining_whenSaveTraining_thenValidateTrainerAndTraineeAndSaveTraining() {

        when(trainingDao.save(training))
                .thenReturn(training);

        Training result =
                trainingService.saveTraining(training);

        verify(trainerService)
                .getTrainerById("trainer-1");

        verify(traineeService)
                .getTraineeById("trainee-1");

        verify(trainingDao)
                .save(training);

        assertEquals(training, result);
    }


    @Test
    void givenTrainingWithUnknownTrainer_whenSaveTraining_thenThrowExceptionAndDoNotSave() {

        doThrow(new EntityNotFoundException("Trainer", "trainer-1"))
                .when(trainerService)
                .getTrainerById("trainer-1");

        assertThrows(
                EntityNotFoundException.class,
                () -> trainingService.saveTraining(training)
        );

        verify(trainerService)
                .getTrainerById("trainer-1");


        verifyNoInteractions(traineeService);

        verify(trainingDao, never())
                .save(any());
    }


    @Test
    void givenTrainingWithUnknownTrainee_whenSaveTraining_thenThrowExceptionAndDoNotSave() {

        doThrow(new EntityNotFoundException("Trainee", "trainee-1"))
                .when(traineeService)
                .getTraineeById("trainee-1");

        assertThrows(
                EntityNotFoundException.class,
                () -> trainingService.saveTraining(training)
        );

        verify(trainerService)
                .getTrainerById("trainer-1");

        verify(traineeService)
                .getTraineeById("trainee-1");

        verify(trainingDao, never())
                .save(any());
    }

    @Test
    void givenValidTraining_whenSaveTraining_thenReturnSavedTraining() {

        Training savedTraining = new Training(
                "trainee-1",
                "trainer-1",
                "Evening strength",
                TrainingType.STRENGTH,
                LocalDateTime.of(2026, 1, 11, 18, 0),
                5400
        );

        when(trainingDao.save(training))
                .thenReturn(savedTraining);


        Training result =
                trainingService.saveTraining(training);

        assertEquals(savedTraining, result);


        verify(trainingDao)
                .save(training);
    }
}