package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.dto.CredentialsDTO;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class AuthenticationServiceImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuthenticationService authenticationService;

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