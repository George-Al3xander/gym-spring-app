package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.dao.impl.UserDaoImpl;
import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.exception.GymEntityNotFoundException;
import io.github.George_Al3xander.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserDaoImpl.class)
class AuthenticationServiceImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDao userDao;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(
                userDao,
                new BCryptPasswordEncoder()
        );

    }

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
                new CredentialsDTO(
                        "unknown_user_" + UUID.randomUUID(),
                        "1234567890"
                )
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
    void givenCorrectOldPassword_whenChangePassword_thenPasswordIsUpdated() {
        String username = unique("john");
        String oldPassword = "oldPass!!!";
        String newPassword = "newPass!!!";

        entityManager.persist(createValidUser(username, oldPassword));
        entityManager.flush();

        authenticationService.changePassword(
                username,
                new ChangeLoginRequest(oldPassword, newPassword)
        );

        entityManager.flush();
        entityManager.clear();

        User updatedUser = userDao.findByUsername(username).orElseThrow();

        assertTrue(
                passwordEncoder.matches(
                        newPassword,
                        updatedUser.getPassword()
                )
        );
    }

    @Test
    void givenWrongOldPassword_whenChangePassword_thenThrowBadCredentialsException() {
        String username = unique("john");

        entityManager.persist(createValidUser(username, "oldPass!!!"));
        entityManager.flush();

        assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.changePassword(
                        username,
                        new ChangeLoginRequest("wrongPassword", "newPass!!!")
                )
        );
    }

    @Test
    void givenUnknownUsername_whenChangePassword_thenThrowGymEntityNotFoundException() {
        String username = "unknown_user_" + UUID.randomUUID();

        assertThrows(
                GymEntityNotFoundException.class,
                () -> authenticationService.changePassword(
                        username,
                        new ChangeLoginRequest("oldPassword", "newPassword")
                )
        );
    }

    private User createValidUser(String username, String password) {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setIsActive(true);

        return user;
    }

    private String unique(String base) {
        return base + "_" + UUID.randomUUID();
    }
}