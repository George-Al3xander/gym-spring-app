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

import java.time.LocalDate;
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

        User user = new User("John", "Smith", null, null, true);

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

        User user = new User("John", "Smith", null, null, true);

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

        User user = new User("John", "Smith", null, null, true);

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

        User user = new User("John", "Smith", null, null, true);

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

        User user = new User("John", "Smith", null, null, true);

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

        User user = new User("John", "Smith", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith1", result);
    }

    @Test
    void given_firstNameContainsLeadingAndTrailingSpaces_when_generateUsername_then_trimFirstName() {
        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("  John  ", "Smith", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_lastNameContainsLeadingAndTrailingSpaces_when_generateUsername_then_trimLastName() {
        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("John", "  Smith  ", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_bothNamesContainLeadingAndTrailingSpaces_when_generateUsername_then_trimNames() {
        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("  John  ", "  Smith  ", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_gapInSuffixSequence_when_generateUsername_then_returnFirstAvailableSuffix() {
        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smith"),
                        trainee("John.Smith2")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("John", "Smith", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith1", result);
    }

    @Test
    void given_similarUsername_when_generateUsername_then_doNotTreatAsDuplicate() {
        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smithers")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("John", "Smith", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_differentFirstName_when_generateUsername_then_doNotTreatAsDuplicate() {
        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("Johnny.Smith")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("John", "Smith", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    @Test
    void given_differentLastName_when_generateUsername_then_doNotTreatAsDuplicate() {
        when(traineeDao.findAll())
                .thenReturn(List.of(
                        trainee("John.Smyth")
                ));

        when(trainerDao.findAll()).thenReturn(List.of());

        User user = new User("John", "Smith", null, null, true);

        String result = generator.generateUsername(user);

        assertEquals("John.Smith", result);
    }

    private Trainee trainee(String username) {
        return new Trainee(
                "Existing",
                "Trainee",
                username,
                "password",
                true,
                LocalDate.of(2000, 1, 1),
                "Address",
                3L
        );
    }

    private Trainer trainer(String username) {
        return new Trainer(
                "Existing",
                "Trainer",
                username,
                "password",
                true,
                2L,
                null
        );
    }
}