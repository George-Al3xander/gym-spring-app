package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
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
    private UsernameGenerator usernameGenerator;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");

        trainerService.setUsernameGenerator(usernameGenerator);
    }

    @Test
    void givenExistingTrainerId_whenGetTrainerById_thenReturnTrainer() {
        String id = "trainer-1";

        when(trainerDao.findById(id))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerById(id);

        assertSame(trainer, result);

        verify(trainerDao)
                .findById(id);
    }

    @Test
    void givenMissingTrainerId_whenGetTrainerById_thenThrowEntityNotFoundException() {
        String id = "missing-id";

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

        trainerService.setUsernameGenerator(customGenerator);

        when(customGenerator.generateUsername(trainer))
                .thenReturn("generated.username");

        when(trainerDao.save(trainer))
                .thenReturn(trainer);

        trainerService.saveTrainer(trainer);

        verify(customGenerator)
                .generateUsername(trainer);
    }
}