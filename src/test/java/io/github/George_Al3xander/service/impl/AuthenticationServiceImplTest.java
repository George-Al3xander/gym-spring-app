package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.AuthenticationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class AuthenticationServiceImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserDao userDao;

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnTrue() {
        String username = unique("john");
        String password = "1234567890";

        entityManager.persist(createValidUser(username, password));
        entityManager.flush();

        boolean result = authenticationService.authenticate(
                new CredentialsDTO(username, password)
        );

        assertTrue(result);
    }

    @Test
    void givenValidUsernameButWrongPassword_whenAuthenticate_thenReturnFalse() {
        String username = unique("john");

        entityManager.persist(createValidUser(username, "1234567890"));
        entityManager.flush();

        boolean result = authenticationService.authenticate(
                new CredentialsDTO(username, "0987654321")
        );

        assertFalse(result);
    }

    @Test
    void givenUnknownUsername_whenAuthenticate_thenReturnFalse() {
        boolean result = authenticationService.authenticate(
                new CredentialsDTO("unknown_user_" + UUID.randomUUID(), "1234567890")
        );

        assertFalse(result);
    }

    @Test
    void givenNullUsername_whenAuthenticate_thenReturnFalse() {
        boolean result = authenticationService.authenticate(
                new CredentialsDTO(null, "1234567890")
        );

        assertFalse(result);
    }

    @Test
    void givenNullPassword_whenAuthenticate_thenReturnFalse() {
        String username = unique("john");

        entityManager.persist(createValidUser(username, "1234567890"));
        entityManager.flush();

        boolean result = authenticationService.authenticate(
                new CredentialsDTO(username, null)
        );

        assertFalse(result);
    }

    @Test
    void givenBothCredentialsNull_whenAuthenticate_thenReturnFalse() {
        boolean result = authenticationService.authenticate(
                new CredentialsDTO(null, null)
        );

        assertFalse(result);
    }

    @Test
    void givenExistingUser_whenChangePassword_thenPasswordIsUpdated() {
        String username = unique("john");
        String oldPassword = "oldPass!!!";
        String newPassword = "newPass!!!";

        entityManager.persist(createValidUser(username, oldPassword));
        entityManager.flush();

        authenticationService.changePassword(
                new CredentialsDTO(username, newPassword)
        );

        entityManager.flush();
        entityManager.clear();

        User updatedUser = userDao.findByUsername(username).orElseThrow();

        assertEquals(newPassword, updatedUser.getPassword());
    }

    @Test
    void givenUnknownUsername_whenChangePassword_thenThrowNoResultException() {
        CredentialsDTO credentials = new CredentialsDTO(
                "unknown_user_" + UUID.randomUUID(),
                "newPassword"
        );

        assertThrows(
                EntityNotFoundException.class,
                () -> authenticationService.changePassword(credentials)
        );
    }

    private User createValidUser(String username, String password10) {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(username);
        user.setPassword(password10);
        user.setIsActive(true);
        return user;
    }

    private String unique(String base) {
        return base + "_" + UUID.randomUUID();
    }
}