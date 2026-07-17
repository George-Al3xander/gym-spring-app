package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.dto.trainee.TraineeProfileResponse;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.dto.trainee.TraineeSummaryResponse;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.dto.trainer.UpdateTrainerRequest;
import io.github.George_Al3xander.mapper.TraineeMapper;
import io.github.George_Al3xander.mapper.TrainerMapper;
import io.github.George_Al3xander.mapper.TrainingMapper;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.TrainingType;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import io.github.George_Al3xander.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private TrainingService trainingService;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    private GymFacadeImpl gymFacade;

    @BeforeEach
    void setUp() {
        gymFacade = new GymFacadeImpl(
                userService,
                trainerService,
                traineeService,
                trainingService,
                trainingTypeDao,
                traineeMapper,
                trainerMapper,
                trainingMapper
        );
    }

    @Test
    void createTrainer_whenValidRequest_thenMapsRequestAndDelegatesToTrainerService() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setSpecializationId(1L);

        Trainer mappedTrainer = newTrainer(null, "john.doe");
        Trainer savedTrainer = newTrainer(1L, "john.doe");

        when(trainerMapper.toTrainer(request))
                .thenReturn(mappedTrainer);

        when(trainerService.saveTrainer(mappedTrainer))
                .thenReturn(savedTrainer);

        when(trainingTypeDao.findById(any(Long.class)))
                .thenReturn(Optional.of(new TrainingType(1L, "Training")));

        Trainer result = gymFacade.createTrainer(request);

        assertEquals(savedTrainer, result);

        verify(trainerMapper)
                .toTrainer(request);

        verify(trainerService)
                .saveTrainer(mappedTrainer);
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

        verify(traineeMapper)
                .toTrainee(request);

        verify(traineeService)
                .saveTrainee(mappedTrainee);
    }

    @Test
    void getTrainer_whenUsernameExists_thenReturnsTrainerProfile() {
        String username = "trainer.mike";

        Trainer trainer = newTrainer(1L, username);

        Trainee trainee = newTrainee(2L, "trainee.anna");

        TraineeSummaryResponse traineeSummary =
                new TraineeSummaryResponse();

        TrainerProfileResponse expected =
                new TrainerProfileResponse();

        when(trainerService.getTrainerByUsername(username))
                .thenReturn(trainer);

        when(traineeService.getTraineesByTrainerUsername(username, true))
                .thenReturn(List.of(trainee));

        when(traineeMapper.toSummary(trainee))
                .thenReturn(traineeSummary);

        when(trainerMapper.toTrainerProfile(
                trainer,
                List.of(traineeSummary)))
                .thenReturn(expected);

        TrainerProfileResponse result =
                gymFacade.getTrainer(username);

        assertEquals(expected, result);

        verify(trainerService)
                .getTrainerByUsername(username);

        verify(traineeService)
                .getTraineesByTrainerUsername(username, true);

        verify(traineeMapper)
                .toSummary(trainee);

        verify(trainerMapper)
                .toTrainerProfile(
                        trainer,
                        List.of(traineeSummary));
    }

    @Test
    void getTrainee_whenUsernameExists_thenReturnsTraineeProfile() {
        String username = "trainee.anna";

        Trainee trainee = newTrainee(1L, username);

        Trainer trainer = newTrainer(2L, "trainer.mike");

        TrainerSummaryResponse trainerSummary =
                new TrainerSummaryResponse();

        TraineeProfileResponse expected =
                new TraineeProfileResponse();

        when(traineeService.getTraineeByUsername(username))
                .thenReturn(trainee);

        when(trainerService.getTrainersByTraineeUsername(username, null))
                .thenReturn(List.of(trainer));

        when(trainerMapper.toSummary(trainer))
                .thenReturn(trainerSummary);

        when(traineeMapper.toTraineeProfile(
                trainee,
                List.of(trainerSummary)))
                .thenReturn(expected);

        TraineeProfileResponse result =
                gymFacade.getTrainee(username);

        assertEquals(expected, result);

        verify(traineeService)
                .getTraineeByUsername(username);

        verify(trainerService)
                .getTrainersByTraineeUsername(username, null);

        verify(trainerMapper)
                .toSummary(trainer);

        verify(traineeMapper)
                .toTraineeProfile(
                        trainee,
                        List.of(trainerSummary));
    }

    @Test
    void updateTrainer_whenUsernameExists_thenUpdatesTrainerAndReturnsProfile() {
        String username = "trainer.mike";

        UpdateTrainerRequest request = new UpdateTrainerRequest();
        request.setFirstName("Michael");
        request.setLastName("Smith");
        request.setIsActive(true);

        Trainer trainer = newTrainer(1L, username);

        Trainee trainee = newTrainee(2L, "trainee.anna");

        TraineeSummaryResponse traineeSummary =
                new TraineeSummaryResponse();

        TrainerProfileResponse expected =
                new TrainerProfileResponse();

        when(trainerService.getTrainerByUsername(username))
                .thenReturn(trainer);

        when(traineeService.getTraineesByTrainerUsername(username, true))
                .thenReturn(List.of(trainee));

        when(traineeMapper.toSummary(trainee))
                .thenReturn(traineeSummary);

        when(trainerMapper.toTrainerProfile(
                trainer,
                List.of(traineeSummary)))
                .thenReturn(expected);

        TrainerProfileResponse result =
                gymFacade.updateTrainer(username, request);

        assertEquals(expected, result);

        assertEquals("Michael", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals(true, trainer.getIsActive());

        verify(trainerService)
                .getTrainerByUsername(username);

        verify(trainerService)
                .updateTrainer(trainer);

        verify(traineeService)
                .getTraineesByTrainerUsername(username, true);

        verify(traineeMapper)
                .toSummary(trainee);

        verify(trainerMapper)
                .toTrainerProfile(
                        trainer,
                        List.of(traineeSummary));
    }

    @Test
    void resetUserPassword_whenUserIdProvided_thenCallsUserServiceResetPassword() {
        gymFacade.resetUserPassword(42L);

        verify(userService)
                .resetPassword(42L);
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