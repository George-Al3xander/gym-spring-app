package io.github.George_Al3xander.service;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private UsernameGenerator generator;

    @Test
    void given_noExistingUsers_when_generateUsername_then_returnBaseUsername() {

        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_duplicateUsernameInTrainees_when_generateUsername_then_appendSuffixOne() {

        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smith")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith1", result);
    }

    @Test
    void given_duplicateUsernameInTrainers_when_generateUsername_then_appendSuffixOne() {

        when(traineeDao.findAll()).thenReturn(List.of());

        when(trainerDao.findAll())
                .thenReturn(List.of(
                        trainer("John.Smith")
                ));

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith1", result);
    }

    @Test
    void given_duplicatesAcrossRepositories_when_generateUsername_then_returnNextAvailableSuffix() {

        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smith")
                ));

        when(trainerDao.findAll())
                .thenReturn(List.of(
                        trainer("John.Smith1")
                ));

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith2", result);
    }

    @Test
    void given_multipleSequentialDuplicates_when_generateUsername_then_returnNextAvailableSuffix() {

        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smith"),
                        trainee("John.Smith1")
                ));

        when(trainerDao.findAll())
                .thenReturn(List.of(
                        trainer("John.Smith2")
                ));

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith3", result);
    }

    @Test
    void given_caseInsensitiveDuplicate_when_generateUsername_then_appendSuffixOne() {

        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("john.smith")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith1", result);
    }

    @Test
    void given_names_with_spaces_when_generateUsername_then_trimNames() {

        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User(
                null,
                "  John  ",
                "  Smith  ",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_similarUsername_when_generateUsername_then_doNotTreatAsDuplicate() {

        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smithers")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User(
                null,
                "John",
                "Smith",
                null,
                "pass",
                true
        );

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    private Trainee trainee(String username) {
        Trainee t = new Trainee();
        t.setUsername(username);
        return t;
    }

    private Trainer trainer(String username) {
        Trainer t = new Trainer();
        t.setUsername(username);
        return t;
    }
}