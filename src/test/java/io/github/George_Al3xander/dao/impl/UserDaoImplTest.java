package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserDaoImpl.class)
class UserDaoImplTest {

    @Autowired
    private UserDaoImpl userDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void givenValidUser_whenSave_thenUserPersistedWithGeneratedId() {
        User user = createUser("john", "John", "Doe");

        assertNotNull(user.getId());

        User dbUser = entityManager.find(User.class, user.getId());

        assertNotNull(dbUser);
        assertEquals("john", dbUser.getUsername());
    }

    @Test
    void givenExistingUser_whenFindById_thenUserReturned() {
        User user = createUser("john", "John", "Doe");

        Optional<User> result = userDao.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void givenMissingUser_whenFindById_thenEmptyOptional() {
        Optional<User> result = userDao.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleUsers_whenFindAll_thenAllUsersReturned() {
        createUser("u1", "A", "A");
        createUser("u2", "B", "B");

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void givenExistingUser_whenDelete_thenUserRemovedFromDatabase() {
        User user = createUser("john", "John", "Doe");

        userDao.delete(user.getId());

        entityManager.flush();
        entityManager.clear();

        User deletedUser = entityManager.find(User.class, user.getId());

        assertNull(deletedUser);
    }

    @Test
    void givenUser_whenUpdate_thenFieldsUpdatedInDatabase() {
        User user = createUser("john", "John", "Doe");

        user.setUsername("john_updated");
        user.setFirstName("Johnny");

        userDao.update(user);

        entityManager.flush();
        entityManager.clear();

        User updatedUser = entityManager.find(User.class, user.getId());

        assertEquals("john_updated", updatedUser.getUsername());
        assertEquals("Johnny", updatedUser.getFirstName());
    }

    @Test
    void givenExistingUsername_whenFindByUsername_thenUserReturned() {
        createUser("john", "John", "Doe");

        Optional<User> result = userDao.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void givenExistingUsername_whenExistsByUsername_thenReturnsTrue() {
        createUser("john", "John", "Doe");

        boolean exists = userDao.existsByUsername("john");

        assertTrue(exists);
    }

    @Test
    void givenMissingUsername_whenExistsByUsername_thenReturnsFalse() {
        boolean exists = userDao.existsByUsername("missing");

        assertFalse(exists);
    }

    private User createUser(String username, String firstName, String lastName) {
        User user = new User();

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword("1234567890");
        user.setIsActive(true);

        entityManager.persist(user);
        entityManager.flush();

        return user;
    }
}