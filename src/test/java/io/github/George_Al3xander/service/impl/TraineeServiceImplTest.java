package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityInUseException;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private UsernameGenerator usernameGenerator;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeService = new TraineeServiceImpl(
                traineeDao,
                trainerDao,
                trainingDao,
                usernameGenerator
        );

        trainee = new Trainee(
                1L,
                "John",
                "Doe",
                "john.doe",
                "pass",
                true,
                LocalDate.of(1990, 1, 1),
                "Kyiv"
        );
    }

    @Test
    void givenExistingTraineeId_whenGetTraineeById_thenReturnTrainee() {

        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        Trainee result =
                traineeService.getTraineeById(1L);

        assertEquals(trainee, result);

        verify(traineeDao)
                .findById(1L);
    }

    @Test
    void givenNonExistingTraineeId_whenGetTraineeById_thenThrowEntityNotFoundException() {

        when(traineeDao.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> traineeService.getTraineeById(1L)
        );

        verify(traineeDao)
                .findById(1L);
    }

    @Test
    void givenExistingTrainees_whenGetAllTrainees_thenReturnTraineeList() {

        List<Trainee> trainees =
                List.of(new Trainee(), new Trainee());

        when(traineeDao.findAll())
                .thenReturn(trainees);

        List<Trainee> result =
                traineeService.getAllTrainees();

        assertEquals(2, result.size());

        verify(traineeDao)
                .findAll();
    }

    @Test
    void givenNoExistingTrainees_whenGetAllTrainees_thenReturnEmptyList() {

        when(traineeDao.findAll())
                .thenReturn(List.of());

        List<Trainee> result =
                traineeService.getAllTrainees();

        assertTrue(result.isEmpty());

        verify(traineeDao)
                .findAll();
    }

    @Test
    void givenValidTrainee_whenSaveTrainee_thenGenerateUsernameAndPassword() {

        Trainee trainee = new Trainee();

        when(usernameGenerator.generateUsername(trainee))
                .thenReturn("john.doe");

        when(traineeDao.save(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result =
                traineeService.saveTrainee(trainee);

        assertEquals("john.doe", result.getUsername());
        assertNotNull(result.getPassword());

        verify(usernameGenerator)
                .generateUsername(trainee);

        verify(traineeDao)
                .save(trainee);
    }

    @Test
    void givenValidTrainee_whenSaveTrainee_thenPassGeneratedDataToDao() {

        Trainee trainee = new Trainee();

        when(usernameGenerator.generateUsername(any()))
                .thenReturn("generated.username");

        when(traineeDao.save(any()))
                .thenReturn(trainee);

        traineeService.saveTrainee(trainee);

        ArgumentCaptor<Trainee> captor =
                ArgumentCaptor.forClass(Trainee.class);

        verify(traineeDao)
                .save(captor.capture());

        assertEquals(
                "generated.username",
                captor.getValue().getUsername()
        );

        assertNotNull(
                captor.getValue().getPassword()
        );
    }

    @Test
    void givenExistingTrainee_whenUpdateTrainee_thenSaveUpdatedTrainee() {

        Trainee trainee = new Trainee();

        when(traineeDao.save(trainee))
                .thenReturn(trainee);

        Trainee result =
                traineeService.updateTrainee(trainee);

        assertEquals(trainee, result);

        verify(traineeDao)
                .save(trainee);
    }

    @Test
    void givenTraineeWithoutTraining_whenDeleteTrainee_thenDeleteTrainee() {

        when(trainingDao.findAll())
                .thenReturn(List.of());

        traineeService.deleteTrainee(1L);

        verify(traineeDao)
                .delete(1L);
    }

    @Test
    void givenTraineeWithTraining_whenDeleteTrainee_thenThrowEntityInUseException() {

        Training training = new Training();

        Trainee t = new Trainee();
        t.setId(1L);

        training.setTrainee(t);

        when(trainingDao.findAll())
                .thenReturn(List.of(training));

        assertThrows(
                EntityInUseException.class,
                () -> traineeService.deleteTrainee(1L)
        );

        verify(traineeDao, never())
                .delete(anyLong());
    }

    @Test
    void givenTraineeWithTrainingUsingDifferentCaseId_whenDeleteTrainee_thenThrowEntityInUseException() {

        Training training = new Training();

        Trainee t = new Trainee();
        t.setId(123L);

        training.setTrainee(t);

        when(trainingDao.findAll())
                .thenReturn(List.of(training));

        assertThrows(
                EntityInUseException.class,
                () -> traineeService.deleteTrainee(123L)
        );

        verify(traineeDao, never())
                .delete(anyLong());
    }

    @Test
    void givenTrainingBelongsToAnotherTrainee_whenDeleteTrainee_thenDeleteTrainee() {

        Training training = new Training();

        Trainee t = new Trainee();
        t.setId(999L);

        training.setTrainee(t);

        when(trainingDao.findAll())
                .thenReturn(List.of(training));

        traineeService.deleteTrainee(1L);

        verify(traineeDao)
                .delete(1L);
    }

    @Test
    void givenExistingUsername_whenGetTraineeByUsername_thenReturnTrainee() {

        when(traineeDao.findByUsername("john.doe"))
                .thenReturn(Optional.of(trainee));

        Trainee result =
                traineeService.getTraineeByUsername("john.doe");

        assertEquals(trainee, result);

        verify(traineeDao).findByUsername("john.doe");
    }

    @Test
    void givenNonExistingUsername_whenGetTraineeByUsername_thenThrowEntityNotFoundException() {

        when(traineeDao.findByUsername("missing.user"))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> traineeService.getTraineeByUsername("missing.user")
        );

        verify(traineeDao).findByUsername("missing.user");
    }

    @Test
    void givenExistingTrainer_whenGetTraineesByTrainerUsername_thenReturnTrainees() {

        Trainer trainer = new Trainer();
        trainer.setUsername("trainer.user");

        List<Trainee> trainees = List.of(new Trainee(), new Trainee());

        when(trainerDao.findByUsername("trainer.user"))
                .thenReturn(Optional.of(trainer));

        when(traineeDao.findAllByTrainerUsername("trainer.user", true))
                .thenReturn(trainees);

        List<Trainee> result =
                traineeService.getTraineesByTrainerUsername("trainer.user", true);

        assertEquals(trainees, result);

        verify(trainerDao)
                .findByUsername("trainer.user");

        verify(traineeDao)
                .findAllByTrainerUsername("trainer.user", true);
    }

    @Test
    void givenNonExistingTrainer_whenGetTraineesByTrainerUsername_thenThrowEntityNotFoundException() {

        when(trainerDao.findByUsername("missing.trainer"))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> traineeService.getTraineesByTrainerUsername("missing.trainer", true)
        );

        verify(trainerDao)
                .findByUsername("missing.trainer");

        verify(traineeDao, never())
                .findAllByTrainerUsername(anyString(), anyBoolean());
    }
}