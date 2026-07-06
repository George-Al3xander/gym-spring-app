package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.service.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UsernameGenerator usernameGenerator;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerService = new TrainerServiceImpl(trainerDao, traineeDao, usernameGenerator);
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");


    }

    @Test
    void givenExistingTrainerId_whenGetTrainerById_thenReturnTrainer() {
        long id = 12L;

        when(trainerDao.findById(id))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerById(id);

        assertSame(trainer, result);

        verify(trainerDao)
                .findById(id);
    }

    @Test
    void givenMissingTrainerId_whenGetTrainerById_thenThrowEntityNotFoundException() {
        long id = 0L;

        when(trainerDao.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> trainerService.getTrainerById(id)
        );

        verify(trainerDao)
                .findById(id);
    }


    @Test
    void givenExistingTrainers_whenGetAllTrainers_thenReturnTrainerList() {
        List<Trainer> trainers = List.of(trainer);

        when(trainerDao.findAll())
                .thenReturn(trainers);

        List<Trainer> result = trainerService.getAllTrainers();

        assertEquals(1, result.size());
        assertEquals(trainer, result.get(0));

        verify(trainerDao)
                .findAll();
    }

    @Test
    void givenNoTrainers_whenGetAllTrainers_thenReturnEmptyList() {
        when(trainerDao.findAll())
                .thenReturn(Collections.emptyList());

        List<Trainer> result = trainerService.getAllTrainers();

        assertTrue(result.isEmpty());

        verify(trainerDao)
                .findAll();
    }

    @Test
    void givenNewTrainer_whenSaveTrainer_thenGenerateCredentialsAndSaveTrainer() {
        String generatedUsername = "john.smith";
        Trainer savedTrainer = new Trainer();

        when(usernameGenerator.generateUsername(trainer))
                .thenReturn(generatedUsername);

        when(trainerDao.save(trainer))
                .thenReturn(savedTrainer);

        Trainer result = trainerService.saveTrainer(trainer);

        assertEquals(
                generatedUsername,
                trainer.getUsername()
        );

        assertNotNull(
                trainer.getPassword()
        );

        assertEquals(
                savedTrainer,
                result
        );

        verify(usernameGenerator)
                .generateUsername(trainer);

        verify(trainerDao)
                .save(trainer);
    }

    @Test
    void givenTrainer_whenSaveTrainer_thenGeneratePasswordWithTenCharacters() {
        when(usernameGenerator.generateUsername(trainer))
                .thenReturn("john.smith");

        when(trainerDao.save(trainer))
                .thenReturn(trainer);

        trainerService.saveTrainer(trainer);

        assertNotNull(trainer.getPassword());

        assertEquals(
                10,
                trainer.getPassword().length()
        );
    }

    @Test
    void givenExistingTrainer_whenUpdateTrainer_thenSaveAndReturnUpdatedTrainer() {
        Trainer updatedTrainer = new Trainer();

        when(trainerDao.save(updatedTrainer))
                .thenReturn(updatedTrainer);

        Trainer result = trainerService.updateTrainer(updatedTrainer);

        assertSame(
                updatedTrainer,
                result
        );

        verify(trainerDao)
                .save(updatedTrainer);

        verifyNoInteractions(usernameGenerator);
    }

    @Test
    void givenUsernameGenerator_whenSetUsernameGenerator_thenUseGeneratorDuringSave() {
        UsernameGenerator customGenerator =
                mock(UsernameGenerator.class);

        trainerService = new TrainerServiceImpl(trainerDao, traineeDao, customGenerator);


        when(customGenerator.generateUsername(trainer))
                .thenReturn("generated.username");

        when(trainerDao.save(trainer))
                .thenReturn(trainer);

        trainerService.saveTrainer(trainer);

        verify(customGenerator)
                .generateUsername(trainer);
    }

    @Test
    void givenExistingUsername_whenGetTrainerByUsername_thenReturnTrainer() {

        String username = "john.smith";

        when(trainerDao.findByUsername(username))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerByUsername(username);

        assertSame(trainer, result);

        verify(trainerDao).findByUsername(username);
    }

    @Test
    void givenMissingUsername_whenGetTrainerByUsername_thenThrowEntityNotFoundException() {

        String username = "missing.user";

        when(trainerDao.findByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> trainerService.getTrainerByUsername(username)
        );

        verify(trainerDao).findByUsername(username);
    }

    @Test
    void givenExistingTrainee_whenGetUnassignedTrainers_thenReturnTrainerList() {
        String username = "john.doe";

        Trainee trainee = new Trainee();
        trainee.setUsername(username);

        List<Trainer> expected = List.of(new Trainer(), new Trainer());

        when(traineeDao.findByUsername(username))
                .thenReturn(Optional.of(trainee));

        when(trainerDao.findUnassignedByTraineeUsername(username))
                .thenReturn(expected);

        List<Trainer> result =
                trainerService.getUnassignedTrainersByTraineeUsername(username);

        assertEquals(expected, result);

        verify(traineeDao).findByUsername(username);
        verify(trainerDao).findUnassignedByTraineeUsername(username);
    }

    @Test
    void givenMissingTrainee_whenGetUnassignedTrainers_thenThrowEntityNotFoundException() {

        String username = "missing.user";

        when(traineeDao.findByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> trainerService.getUnassignedTrainersByTraineeUsername(username)
        );

        verify(traineeDao).findByUsername(username);
        verifyNoInteractions(trainerDao);
    }
}