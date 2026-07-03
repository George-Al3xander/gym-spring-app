package io.github.George_Al3xander.service;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfig.class)
@Transactional
class UsernameGeneratorTest {

    @Autowired
    private UsernameGenerator usernameGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void givenNewUser_whenGenerateUsername_thenReturnsBaseUsername() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        String result = usernameGenerator.generateUsername(user);

        assertEquals("John.Doe", result);
    }

    @Test
    void givenExistingUserWithSameName_whenGenerateUsername_thenAppendsCount() {
        persistUserToDB("john.doe", "John", "Doe");

        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setPassword("1234567890");

        String result = usernameGenerator.generateUsername(newUser);

        assertEquals("John.Doe1", result);
    }

    @Test
    void givenMultipleExistingUsers_whenGenerateUsername_thenCorrectIncrementedSuffix() {
        persistUserToDB("john.doe", "John", "Doe");
        persistUserToDB("john.doe1", "John", "Doe");

        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setPassword("1234567890");

        String result = usernameGenerator.generateUsername(newUser);

        assertEquals("John.Doe2", result);
    }

    @Test
    void givenNamesWithWhitespace_whenGenerateUsername_thenTrimsAndBuildsBaseUsername() {
        User user = new User();
        user.setFirstName("  John  ");
        user.setLastName("  Doe  ");
        user.setPassword("1234567890");

        String result = usernameGenerator.generateUsername(user);

        assertEquals("John.Doe", result);
    }

    @Test
    void givenExistingUsersInDatabase_whenGenerateUsername_thenUsesDatabaseCount() {
        persistUserToDB("john.doe", "John", "Doe");
        persistUserToDB("john.doe1", "John", "Doe");
        persistUserToDB("john.doe2", "John", "Doe");

        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");

        String result = usernameGenerator.generateUsername(newUser);

        assertEquals("John.Doe3", result);
    }

    @Test
    void givenDifferentName_whenGenerateUsername_thenIgnoresOtherUsers() {
        persistUserToDB("john.doe", "John", "Doe");

        User newUser = new User();
        newUser.setFirstName("Jane");
        newUser.setLastName("Smith");

        String result = usernameGenerator.generateUsername(newUser);

        assertEquals("Jane.Smith", result);
    }

    @Test
    void givenEmptySpacesOnly_whenGenerateUsername_thenProducesDotOnlyOrInvalidBase() {
        User user = new User();
        user.setFirstName("   ");
        user.setLastName("   ");

        String result = usernameGenerator.generateUsername(user);

        assertEquals(".", result);
    }

    private User persistUserToDB(String username, String firstName, String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword("secret");
        user.setIsActive(true);
        user.setPassword("1234567890");

        entityManager.persist(user);
        return user;
    }
}