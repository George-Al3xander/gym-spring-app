package io.github.George_Al3xander.facade.impl;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.*;
import io.github.George_Al3xander.dto.trainer.*;
import io.github.George_Al3xander.dto.training.AddTrainingRequest;
import io.github.George_Al3xander.exception.GymEntityNotFoundException;
import io.github.George_Al3xander.mapper.TraineeMapper;
import io.github.George_Al3xander.mapper.TrainerMapper;
import io.github.George_Al3xander.mapper.TrainingMapper;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.model.TrainingType;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.TrainingService;
import io.github.George_Al3xander.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private GymFacadeImpl gymFacade;

    private static final String TRAINEE_USERNAME = "jane.smith";
    private static final String TRAINER_USERNAME = "john.doe";

    @Nested
    class CreateTrainer {

        @Test
        void givenValidRegistrationRequest_whenCreateTrainer_thenSpecializationIsSetAndCredentialsReturned() {
            TrainerRegistrationRequest request = mock(TrainerRegistrationRequest.class);
            when(request.getSpecializationId()).thenReturn(10L);

            Trainer mappedTrainer = mock(Trainer.class);
            when(trainerMapper.toTrainer(request)).thenReturn(mappedTrainer);

            TrainingType trainingType = mock(TrainingType.class);
            when(trainingTypeDao.findById(10L)).thenReturn(Optional.of(trainingType));

            CredentialsDTO expectedCredentials = mock(CredentialsDTO.class);
            when(trainerService.saveTrainer(mappedTrainer)).thenReturn(expectedCredentials);

            CredentialsDTO result = gymFacade.createTrainer(request);

            assertEquals(expectedCredentials, result);
            verify(mappedTrainer).setSpecialization(trainingType);
            verify(trainerService).saveTrainer(mappedTrainer);
        }

        @Test
        void givenNonExistentSpecializationId_whenCreateTrainer_thenThrowsGymEntityNotFoundException() {
            TrainerRegistrationRequest request = mock(TrainerRegistrationRequest.class);
            when(request.getSpecializationId()).thenReturn(999L);

            Trainer mappedTrainer = mock(Trainer.class);
            when(trainerMapper.toTrainer(request)).thenReturn(mappedTrainer);
            when(trainingTypeDao.findById(999L)).thenReturn(Optional.empty());

            assertThrows(GymEntityNotFoundException.class, () -> gymFacade.createTrainer(request));

            verify(trainerService, never()).saveTrainer(any());
        }
    }

    @Nested
    class CreateTrainee {

        @Test
        void givenValidRegistrationRequest_whenCreateTrainee_thenCredentialsReturned() {
            TraineeRegistrationRequest request = mock(TraineeRegistrationRequest.class);
            Trainee mappedTrainee = mock(Trainee.class);
            when(traineeMapper.toTrainee(request)).thenReturn(mappedTrainee);

            CredentialsDTO expectedCredentials = mock(CredentialsDTO.class);
            when(traineeService.saveTrainee(mappedTrainee)).thenReturn(expectedCredentials);

            CredentialsDTO result = gymFacade.createTrainee(request);

            assertEquals(expectedCredentials, result);
            verify(traineeService).saveTrainee(mappedTrainee);
        }
    }

    @Nested
    class GetTrainer {

        @Test
        void givenExistingUsername_whenGetTrainer_thenReturnsProfileWithTraineesList() {
            Trainer trainer = mock(Trainer.class);
            when(trainerService.getTrainerByUsername(TRAINER_USERNAME)).thenReturn(trainer);

            Trainee trainee1 = mock(Trainee.class);
            TraineeSummaryResponse summary1 = mock(TraineeSummaryResponse.class);
            when(traineeService.getTraineesByTrainerUsername(TRAINER_USERNAME, true))
                    .thenReturn(List.of(trainee1));
            when(traineeMapper.toSummary(trainee1)).thenReturn(summary1);

            TrainerProfileResponse expectedProfile = mock(TrainerProfileResponse.class);
            when(trainerMapper.toTrainerProfile(eq(trainer), eq(List.of(summary1))))
                    .thenReturn(expectedProfile);

            TrainerProfileResponse result = gymFacade.getTrainer(TRAINER_USERNAME);

            assertEquals(expectedProfile, result);
            verify(traineeService).getTraineesByTrainerUsername(TRAINER_USERNAME, true);
        }

        @Test
        void givenTrainerWithNoTrainees_whenGetTrainer_thenReturnsProfileWithEmptyTraineesList() {
            Trainer trainer = mock(Trainer.class);
            when(trainerService.getTrainerByUsername(TRAINER_USERNAME)).thenReturn(trainer);
            when(traineeService.getTraineesByTrainerUsername(TRAINER_USERNAME, true))
                    .thenReturn(Collections.emptyList());

            TrainerProfileResponse expectedProfile = mock(TrainerProfileResponse.class);
            when(trainerMapper.toTrainerProfile(eq(trainer), eq(Collections.emptyList())))
                    .thenReturn(expectedProfile);

            TrainerProfileResponse result = gymFacade.getTrainer(TRAINER_USERNAME);

            assertEquals(expectedProfile, result);
            verify(traineeMapper, never()).toSummary(any());
        }

        @Test
        void givenNonExistentUsername_whenGetTrainer_thenPropagatesException() {
            when(trainerService.getTrainerByUsername("unknown"))
                    .thenThrow(new GymEntityNotFoundException("Trainer", "unknown"));

            assertThrows(GymEntityNotFoundException.class, () -> gymFacade.getTrainer("unknown"));

            verifyNoInteractions(traineeService);
        }
    }

    @Nested
    class GetTrainee {

        @Test
        void givenExistingUsername_whenGetTrainee_thenReturnsProfileWithTrainersList() {
            Trainee trainee = mock(Trainee.class);
            when(traineeService.getTraineeByUsername(TRAINEE_USERNAME)).thenReturn(trainee);

            Trainer trainer1 = mock(Trainer.class);
            TrainerSummaryResponse summary1 = mock(TrainerSummaryResponse.class);
            when(trainerService.getTrainersByTraineeUsername(eq(TRAINEE_USERNAME), any(TrainerFilter.class)))
                    .thenReturn(List.of(trainer1));
            when(trainerMapper.toSummary(trainer1)).thenReturn(summary1);

            TraineeProfileResponse expectedProfile = mock(TraineeProfileResponse.class);
            when(traineeMapper.toTraineeProfile(eq(trainee), eq(List.of(summary1))))
                    .thenReturn(expectedProfile);

            TraineeProfileResponse result = gymFacade.getTrainee(TRAINEE_USERNAME);

            assertEquals(expectedProfile, result);
        }

        @Test
        void givenNonExistentUsername_whenGetTrainee_thenPropagatesException() {
            when(traineeService.getTraineeByUsername("unknown"))
                    .thenThrow(new GymEntityNotFoundException("Trainee", "unknown"));

            assertThrows(GymEntityNotFoundException.class, () -> gymFacade.getTrainee("unknown"));

            verifyNoInteractions(trainerService);
        }
    }

    @Test
    void givenValidId_whenResetUserPassword_thenDelegatesToUserService() {
        gymFacade.resetUserPassword(42L);

        verify(userService).resetPassword(42L);
    }

    @Test
    void givenValidRequest_whenUpdateTrainer_thenFieldsUpdatedAndProfileReturned() {
        Trainer trainer = mock(Trainer.class);
        when(trainerService.getTrainerByUsername(TRAINER_USERNAME)).thenReturn(trainer);

        UpdateTrainerRequest request = mock(UpdateTrainerRequest.class);
        when(request.getFirstName()).thenReturn("John");
        when(request.getLastName()).thenReturn("Doe");
        when(request.getIsActive()).thenReturn(true);

        when(traineeService.getTraineesByTrainerUsername(TRAINER_USERNAME, true))
                .thenReturn(Collections.emptyList());

        TrainerProfileResponse expectedProfile = mock(TrainerProfileResponse.class);
        when(trainerMapper.toTrainerProfile(eq(trainer), eq(Collections.emptyList())))
                .thenReturn(expectedProfile);

        TrainerProfileResponse result = gymFacade.updateTrainer(TRAINER_USERNAME, request);

        verify(trainer).setFirstName("John");
        verify(trainer).setLastName("Doe");
        verify(trainer).setIsActive(true);
        verify(trainerService).updateTrainer(trainer);
        assertEquals(expectedProfile, result);
    }

    @Nested
    class UpdateTrainee {

        @Test
        void givenRequestWithAddressAndDob_whenUpdateTrainee_thenAllFieldsUpdated() {
            Trainee trainee = mock(Trainee.class);
            when(traineeService.getTraineeByUsername(TRAINEE_USERNAME)).thenReturn(trainee);

            UpdateTraineeRequest request = mock(UpdateTraineeRequest.class);
            when(request.getFirstName()).thenReturn("Jane");
            when(request.getLastName()).thenReturn("Smith");
            when(request.getIsActive()).thenReturn(true);
            when(request.getAddress()).thenReturn("123 Main St");
            LocalDate dob = LocalDate.of(1995, 5, 20);
            when(request.getDateOfBirth()).thenReturn(dob);

            when(trainerService.getTrainersByTraineeUsername(eq(TRAINEE_USERNAME), any(TrainerFilter.class)))
                    .thenReturn(Collections.emptyList());

            TraineeProfileResponse expectedProfile = mock(TraineeProfileResponse.class);
            when(traineeMapper.toTraineeProfile(eq(trainee), eq(Collections.emptyList())))
                    .thenReturn(expectedProfile);

            TraineeProfileResponse result = gymFacade.updateTrainee(TRAINEE_USERNAME, request);

            verify(trainee).setFirstName("Jane");
            verify(trainee).setLastName("Smith");
            verify(trainee).setIsActive(true);
            verify(trainee).setAddress("123 Main St");
            verify(trainee).setDateOfBirth(dob);
            verify(traineeService).updateTrainee(trainee);
            assertEquals(expectedProfile, result);
        }

        @Test
        void givenRequestWithNullAddressAndDob_whenUpdateTrainee_thenAddressAndDobNotOverwritten() {
            Trainee trainee = mock(Trainee.class);
            when(traineeService.getTraineeByUsername(TRAINEE_USERNAME)).thenReturn(trainee);

            UpdateTraineeRequest request = mock(UpdateTraineeRequest.class);
            when(request.getFirstName()).thenReturn("Jane");
            when(request.getLastName()).thenReturn("Smith");
            when(request.getIsActive()).thenReturn(false);
            when(request.getAddress()).thenReturn(null);
            when(request.getDateOfBirth()).thenReturn(null);

            when(trainerService.getTrainersByTraineeUsername(eq(TRAINEE_USERNAME), any(TrainerFilter.class)))
                    .thenReturn(Collections.emptyList());
            when(traineeMapper.toTraineeProfile(eq(trainee), eq(Collections.emptyList())))
                    .thenReturn(mock(TraineeProfileResponse.class));

            gymFacade.updateTrainee(TRAINEE_USERNAME, request);

            verify(trainee).setFirstName("Jane");
            verify(trainee).setLastName("Smith");
            verify(trainee).setIsActive(false);
            verify(trainee, never()).setAddress(any());
            verify(trainee, never()).setDateOfBirth(any());
        }
    }

    @Test
    void givenUsernameAndFlag_whenUpdateActiveStatusByUsername_thenDelegatesToUserService() {
        gymFacade.updateActiveStatusByUsername(TRAINEE_USERNAME, true);

        verify(userService).updateActiveStatusByUsername(TRAINEE_USERNAME, true);
    }

    @Test
    void givenExistingUsername_whenDeleteTrainee_thenDeletesById() {
        Trainee trainee = mock(Trainee.class);
        when(trainee.getId()).thenReturn(7L);
        when(traineeService.getTraineeByUsername(TRAINEE_USERNAME)).thenReturn(trainee);

        gymFacade.deleteTrainee(TRAINEE_USERNAME);

        verify(traineeService).deleteTrainee(7L);
    }

    @Nested
    class GetTraineeTrainings {

        @Test
        void givenTrainingsExist_whenGetTraineeTrainings_thenTrainerNameIsPopulated() {
            TrainingFilter filter = mock(TrainingFilter.class);

            Trainer trainer = mock(Trainer.class);
            when(trainer.getFirstName()).thenReturn("John");
            when(trainer.getLastName()).thenReturn("Doe");

            Training training = mock(Training.class);
            when(training.getTrainer()).thenReturn(trainer);

            when(trainingService.findByTraineeUsername(TRAINEE_USERNAME, filter))
                    .thenReturn(List.of(training));

            TraineeTrainingResponse response = mock(TraineeTrainingResponse.class);
            when(trainingMapper.toTraineeResponse(training)).thenReturn(response);

            List<TraineeTrainingResponse> result = gymFacade.getTraineeTrainings(TRAINEE_USERNAME, filter);

            assertEquals(1, result.size());
            assertEquals(response, result.get(0));
            verify(response).setTrainerName("John Doe");
        }

        @Test
        void givenNoTrainings_whenGetTraineeTrainings_thenReturnsEmptyList() {
            TrainingFilter filter = mock(TrainingFilter.class);
            when(trainingService.findByTraineeUsername(TRAINEE_USERNAME, filter))
                    .thenReturn(Collections.emptyList());

            List<TraineeTrainingResponse> result = gymFacade.getTraineeTrainings(TRAINEE_USERNAME, filter);

            assertTrue(result.isEmpty());
            verify(trainingMapper, never()).toTraineeResponse(any());
        }
    }

    @Nested
    class GetTrainerTrainings {

        @Test
        void givenTrainingsExist_whenGetTrainerTrainings_thenTraineeNameIsPopulated() {
            TrainingFilter filter = mock(TrainingFilter.class);

            Trainee trainee = mock(Trainee.class);
            when(trainee.getFirstName()).thenReturn("Jane");
            when(trainee.getLastName()).thenReturn("Smith");

            Training training = mock(Training.class);
            when(training.getTrainee()).thenReturn(trainee);

            when(trainingService.findByTrainerUsername(TRAINER_USERNAME, filter))
                    .thenReturn(List.of(training));

            TrainerTrainingResponse response = mock(TrainerTrainingResponse.class);
            when(trainingMapper.toTrainerResponse(training)).thenReturn(response);

            List<TrainerTrainingResponse> result = gymFacade.getTrainerTrainings(TRAINER_USERNAME, filter);

            assertEquals(1, result.size());
            assertEquals(response, result.get(0));
            verify(response).setTraineeName("Jane Smith");
        }

        @Test
        void givenNoTrainings_whenGetTrainerTrainings_thenReturnsEmptyList() {
            TrainingFilter filter = mock(TrainingFilter.class);
            when(trainingService.findByTrainerUsername(TRAINER_USERNAME, filter))
                    .thenReturn(Collections.emptyList());

            List<TrainerTrainingResponse> result = gymFacade.getTrainerTrainings(TRAINER_USERNAME, filter);

            assertTrue(result.isEmpty());
            verify(trainingMapper, never()).toTrainerResponse(any());
        }
    }

    @Test
    void givenValidRequest_whenAddTraining_thenTrainerAndTraineeAreLinkedAndSaved() {
        AddTrainingRequest request = mock(AddTrainingRequest.class);
        when(request.getTrainerUsername()).thenReturn(TRAINER_USERNAME);
        when(request.getTraineeUsername()).thenReturn(TRAINEE_USERNAME);

        Training mappedTraining = mock(Training.class);
        when(trainingMapper.toTraining(request)).thenReturn(mappedTraining);

        Trainer trainer = mock(Trainer.class);
        when(trainerService.getTrainerByUsername(TRAINER_USERNAME)).thenReturn(trainer);

        Trainee trainee = mock(Trainee.class);
        when(traineeService.getTraineeByUsername(TRAINEE_USERNAME)).thenReturn(trainee);

        Training savedTraining = mock(Training.class);
        when(trainingService.saveTraining(mappedTraining)).thenReturn(savedTraining);

        Training result = gymFacade.addTraining(request);

        verify(mappedTraining).setTrainer(trainer);
        verify(mappedTraining).setTrainee(trainee);
        verify(trainingService).saveTraining(mappedTraining);
        assertEquals(savedTraining, result);
    }

    @Nested
    class GetTrainersByTraineeUsername {

        @Test
        void givenUsernameAndFilter_whenGetTrainersByTraineeUsername_thenReturnsMappedSummaries() {
            TrainerFilter filter = mock(TrainerFilter.class);
            Trainer trainer = mock(Trainer.class);
            TrainerSummaryResponse summary = mock(TrainerSummaryResponse.class);

            when(trainerService.getTrainersByTraineeUsername(TRAINEE_USERNAME, filter))
                    .thenReturn(List.of(trainer));
            when(trainerMapper.toSummary(trainer)).thenReturn(summary);

            List<TrainerSummaryResponse> result =
                    gymFacade.getTrainersByTraineeUsername(TRAINEE_USERNAME, filter);

            assertEquals(1, result.size());
            assertEquals(summary, result.get(0));
        }

        @Test
        void givenNoTrainersAssigned_whenGetTrainersByTraineeUsername_thenReturnsEmptyList() {
            TrainerFilter filter = mock(TrainerFilter.class);
            when(trainerService.getTrainersByTraineeUsername(TRAINEE_USERNAME, filter))
                    .thenReturn(Collections.emptyList());

            List<TrainerSummaryResponse> result =
                    gymFacade.getTrainersByTraineeUsername(TRAINEE_USERNAME, filter);

            assertTrue(result.isEmpty());
            verify(trainerMapper, never()).toSummary(any());
        }
    }

    @Test
    void givenRequest_whenUpdateTrainersListByTraineeUsername_thenDeletesThenReturnsRefreshedList() {
        UpdateTraineeTrainerListRequest request = mock(UpdateTraineeTrainerListRequest.class);
        List<String> trainersToRemove = List.of("trainer.one", "trainer.two");
        when(request.getTrainers()).thenReturn(trainersToRemove);

        Trainer remainingTrainer = mock(Trainer.class);
        TrainerSummaryResponse remainingSummary = mock(TrainerSummaryResponse.class);
        when(trainerService.getTrainersByTraineeUsername(eq(TRAINEE_USERNAME), any(TrainerFilter.class)))
                .thenReturn(List.of(remainingTrainer));
        when(trainerMapper.toSummary(remainingTrainer)).thenReturn(remainingSummary);

        List<TrainerSummaryResponse> result =
                gymFacade.updateTrainersListByTraineeUsername(TRAINEE_USERNAME, request);

        verify(trainingService).deleteForTraineeByTrainerUsernames(TRAINEE_USERNAME, trainersToRemove);
        assertEquals(1, result.size());
        assertEquals(remainingSummary, result.get(0));
    }
}