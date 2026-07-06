package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private GymFacadeImpl gymFacade;

    private CredentialsDTO credentials;

    @BeforeEach
    void setUp() {
        credentials = new CredentialsDTO("john.doe", "password123");
    }

    @Test
    void createTrainer_whenValidTrainer_thenDelegatesToTrainerServiceAndReturnsSavedTrainer() {
        Trainer input = newTrainer(null, "john.doe");
        Trainer saved = newTrainer(1L, "john.doe");
        when(trainerService.saveTrainer(input)).thenReturn(saved);

        Trainer result = gymFacade.createTrainer(input);

        assertEquals(saved, result);
        verify(trainerService).saveTrainer(input);
        verifyNoInteractions(authenticationService);
    }

    @Test
    void createTrainee_whenValidTrainee_thenDelegatesToTraineeServiceAndReturnsSavedTrainee() {
        Trainee input = newTrainee(null, "jane.doe");
        Trainee saved = newTrainee(1L, "jane.doe");
        when(traineeService.saveTrainee(input)).thenReturn(saved);

        Trainee result = gymFacade.createTrainee(input);

        assertEquals(saved, result);
        verify(traineeService).saveTrainee(input);
        verifyNoInteractions(authenticationService);
    }

    @Test
    void getTrainer_whenCredentialsValid_thenReturnsTrainerByUsername() {
        Trainer trainer = newTrainer(1L, "trainer.mike");
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(trainerService.getTrainerByUsername("trainer.mike")).thenReturn(trainer);

        Trainer result = gymFacade.getTrainer(credentials, "trainer.mike");

        assertEquals(trainer, result);
        verify(trainerService).getTrainerByUsername("trainer.mike");
    }

    @Test
    void getTrainer_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotFetchTrainer() {
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.getTrainer(credentials, "trainer.mike"));

        verifyNoInteractions(trainerService);
    }

    @Test
    void getTrainee_whenCredentialsValid_thenReturnsTraineeByUsername() {
        Trainee trainee = newTrainee(1L, "trainee.anna");
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(traineeService.getTraineeByUsername("trainee.anna")).thenReturn(trainee);

        Trainee result = gymFacade.getTrainee(credentials, "trainee.anna");

        assertEquals(trainee, result);
        verify(traineeService).getTraineeByUsername("trainee.anna");
    }

    @Test
    void getTrainee_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotFetchTrainee() {
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.getTrainee(credentials, "trainee.anna"));

        verifyNoInteractions(traineeService);
    }

    @Test
    void resetUserPassword_whenCredentialsValid_thenCallsUserServiceResetPassword() {
        when(authenticationService.authenticate(credentials)).thenReturn(true);

        gymFacade.resetUserPassword(credentials, 42L);

        verify(userService).resetPassword(42L);
    }

    @Test
    void resetUserPassword_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotResetPassword() {
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.resetUserPassword(credentials, 42L));

        verify(userService, never()).resetPassword(anyLong());
    }

    @Test
    void updateTrainer_whenCredentialsValid_thenReturnsUpdatedTrainer() {
        Trainer toUpdate = newTrainer(1L, "trainer.mike");
        Trainer updated = newTrainer(1L, "trainer.mike");
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(trainerService.updateTrainer(toUpdate)).thenReturn(updated);

        Trainer result = gymFacade.updateTrainer(credentials, toUpdate);

        assertEquals(updated, result);
        verify(trainerService).updateTrainer(toUpdate);
    }

    @Test
    void updateTrainer_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotUpdate() {
        Trainer toUpdate = newTrainer(1L, "trainer.mike");
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.updateTrainer(credentials, toUpdate));

        verify(trainerService, never()).updateTrainer(any());
    }

    @Test
    void updateTrainee_whenCredentialsValid_thenReturnsUpdatedTrainee() {
        Trainee toUpdate = newTrainee(1L, "trainee.anna");
        Trainee updated = newTrainee(1L, "trainee.anna");
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(traineeService.updateTrainee(toUpdate)).thenReturn(updated);

        Trainee result = gymFacade.updateTrainee(credentials, toUpdate);

        assertEquals(updated, result);
        verify(traineeService).updateTrainee(toUpdate);
    }

    @Test
    void updateTrainee_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotUpdate() {
        Trainee toUpdate = newTrainee(1L, "trainee.anna");
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.updateTrainee(credentials, toUpdate));

        verify(traineeService, never()).updateTrainee(any());
    }

    @Test
    void toggleUserActiveStatus_whenCredentialsValid_thenCallsUserServiceToggle() {
        when(authenticationService.authenticate(credentials)).thenReturn(true);

        gymFacade.toggleUserActiveStatus(credentials, "trainee.anna");

        verify(userService).toggleActiveStatusByUsername("trainee.anna");
    }

    @Test
    void toggleUserActiveStatus_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotToggle() {
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.toggleUserActiveStatus(credentials, "trainee.anna"));

        verify(userService, never()).toggleActiveStatusByUsername(any());
    }

    @Test
    void deleteTrainee_whenCredentialsValid_thenFetchesTraineeByUsernameThenDeletesById() {
        Trainee trainee = newTrainee(7L, "trainee.anna");
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(traineeService.getTraineeByUsername("trainee.anna")).thenReturn(trainee);

        gymFacade.deleteTrainee(credentials, "trainee.anna");

        var inOrder = inOrder(traineeService);
        inOrder.verify(traineeService).getTraineeByUsername("trainee.anna");
        inOrder.verify(traineeService).deleteTrainee(7L);
    }

    @Test
    void deleteTrainee_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotDelete() {
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.deleteTrainee(credentials, "trainee.anna"));

        verifyNoInteractions(traineeService);
    }

    @Test
    void getTraineeTrainings_whenCredentialsValid_thenReturnsTrainingsFilteredByCriteriaForAuthenticatedUsername() {
        TrainingFilter filter = TrainingFilter.builder()
                .fromDate(LocalDateTime.of(2026, 1, 1, 0, 0))
                .toDate(LocalDateTime.of(2026, 12, 31, 23, 59))
                .build();
        List<Training> trainings = List.of(new Training());
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(trainingService.findByTraineeUsername(credentials.getUsername(), filter)).thenReturn(trainings);

        List<Training> result = gymFacade.getTraineeTrainings(credentials, filter);

        assertEquals(trainings, result);
        verify(trainingService).findByTraineeUsername(credentials.getUsername(), filter);
    }

    @Test
    void getTraineeTrainings_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotQueryTrainings() {
        TrainingFilter filter = TrainingFilter.builder().build();
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.getTraineeTrainings(credentials, filter));

        verifyNoInteractions(trainingService);
    }

    @Test
    void getTrainerTrainings_whenCredentialsValid_thenReturnsTrainingsFilteredByCriteriaForAuthenticatedUsername() {
        TrainingFilter filter = TrainingFilter.builder().build();
        List<Training> trainings = List.of(new Training());
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(trainingService.findByTrainerUsername(credentials.getUsername(), filter)).thenReturn(trainings);

        List<Training> result = gymFacade.getTrainerTrainings(credentials, filter);

        assertEquals(trainings, result);
        verify(trainingService).findByTrainerUsername(credentials.getUsername(), filter);
    }

    @Test
    void getTrainerTrainings_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotQueryTrainings() {
        TrainingFilter filter = TrainingFilter.builder().build();
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.getTrainerTrainings(credentials, filter));

        verifyNoInteractions(trainingService);
    }

    @Test
    void addTraining_whenCredentialsValid_thenReturnsSavedTraining() {
        Training toSave = new Training();
        toSave.setTrainingName("Cardio Blast");
        Training saved = new Training();
        saved.setId(99L);
        saved.setTrainingName("Cardio Blast");
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(trainingService.saveTraining(toSave)).thenReturn(saved);

        Training result = gymFacade.addTraining(credentials, toSave);

        assertEquals(saved, result);
        verify(trainingService).saveTraining(toSave);
    }

    @Test
    void addTraining_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotSaveTraining() {
        Training toSave = new Training();
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.addTraining(credentials, toSave));

        verify(trainingService, never()).saveTraining(any());
    }

    @Test
    void getUnassignedTrainers_whenCredentialsValid_thenReturnsUnassignedTrainersForTrainee() {
        List<Trainer> trainers = List.of(newTrainer(2L, "trainer.kate"));
        when(authenticationService.authenticate(credentials)).thenReturn(true);
        when(trainerService.getUnassignedTrainersByTraineeUsername("trainee.anna")).thenReturn(trainers);

        List<Trainer> result = gymFacade.getUnassignedTrainers(credentials, "trainee.anna");

        assertEquals(trainers, result);
        verify(trainerService).getUnassignedTrainersByTraineeUsername("trainee.anna");
    }

    @Test
    void getUnassignedTrainers_whenCredentialsInvalid_thenThrowsBadCredentialsExceptionAndDoesNotQueryTrainers() {
        when(authenticationService.authenticate(credentials)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> gymFacade.getUnassignedTrainers(credentials, "trainee.anna"));

        verifyNoInteractions(trainerService);
    }

    private Trainer newTrainer(Long id, String username) {
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setUsername(username);
        return trainer;
    }

    private Trainee newTrainee(Long id, String username) {
        Trainee trainee = new Trainee();
        trainee.setId(id);
        trainee.setUsername(username);
        trainee.setDateOfBirth(LocalDate.of(1995, 3, 20));
        return trainee;
    }
}