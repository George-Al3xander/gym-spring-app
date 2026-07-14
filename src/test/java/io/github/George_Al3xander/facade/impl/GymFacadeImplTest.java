package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.mapper.TraineeMapper;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeImplTest {
    @Mock
    private UserService userService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private GymFacadeImpl gymFacade;

    @Test
    void createTrainer_whenValidTrainer_thenDelegatesToTrainerServiceAndReturnsSavedTrainer() {
        Trainer input = newTrainer(null, "john.doe");
        Trainer saved = newTrainer(1L, "john.doe");
        when(trainerService.saveTrainer(input)).thenReturn(saved);

        Trainer result = gymFacade.createTrainer(input);

        assertEquals(saved, result);
        verify(trainerService).saveTrainer(input);
    }

    @Test
    void createTrainee_whenValidTrainee_thenDelegatesToTraineeServiceAndReturnsSavedTrainee() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        Trainee mappedTrainee = newTrainee(null, "jane.doe");
        Trainee saved = newTrainee(1L, "jane.doe");

        when(traineeMapper.toTrainee(request))
                .thenReturn(mappedTrainee);

        when(traineeService.saveTrainee(mappedTrainee))
                .thenReturn(saved);

        Trainee result = gymFacade.createTrainee(request);

        assertEquals(saved, result);

        verify(traineeMapper).toTrainee(request);
        verify(traineeService).saveTrainee(mappedTrainee);
    }

    @Test
    void getTrainer_whenUsernameExists_thenReturnsTrainerByUsername() {
        Trainer trainer = newTrainer(1L, "trainer.mike");
        when(trainerService.getTrainerByUsername("trainer.mike")).thenReturn(trainer);

        Trainer result = gymFacade.getTrainer("trainer.mike");

        assertEquals(trainer, result);
        verify(trainerService).getTrainerByUsername("trainer.mike");
    }

    @Test
    void getTrainee_whenUsernameExists_thenReturnsTraineeByUsername() {
        Trainee trainee = newTrainee(1L, "trainee.anna");
        when(traineeService.getTraineeByUsername("trainee.anna")).thenReturn(trainee);

        Trainee result = gymFacade.getTrainee("trainee.anna");

        assertEquals(trainee, result);
        verify(traineeService).getTraineeByUsername("trainee.anna");
    }

    @Test
    void resetUserPassword_whenUserIdProvided_thenCallsUserServiceResetPassword() {
        gymFacade.resetUserPassword(42L);

        verify(userService).resetPassword(42L);
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