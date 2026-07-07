package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfig.class)
@Transactional
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void givenValidUser_whenSave_thenUserPersistedWithGeneratedId() {
        User user = new User();
        user.setUsername("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("pass123");
        user.setIsActive(true);

        User savedUser = userDao.save(user);

        assertNotNull(savedUser.getId());

        User dbUser = entityManager.find(User.class, savedUser.getId());
        assertNotNull(dbUser);
        assertEquals("john", dbUser.getUsername());
    }

    @Test
    void givenExistingUser_whenFindById_thenUserReturned() {
        User user = createUser("john", "John", "Doe");

        UserDao userDaoRef = this.userDao;
        Optional<User> result = userDaoRef.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void givenMissingUser_whenFindById_thenEmptyOptional() {
        java.util.Optional<User> result = userDao.findById(999L);

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

        java.util.Optional<User> result = userDao.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void givenMissingUsername_whenFindByUsername_thenThrowsNoResultException() {
        assertThrows(NoResultException.class, () -> userDao.findByUsername("missing"));
    }

    @Test
    void givenUsersWithSameName_whenCountByName_thenReturnsCorrectCount() {
        createUser("u1", "John", "Doe");
        createUser("u2", "John", "Doe");

        long count = userDao.countByName("John", "Doe");

        assertEquals(2L, count);
    }

    @Test
    void givenDifferentCaseNames_whenCountByName_thenCaseInsensitive() {
        createUser("u1", "john", "doe");

        long count = userDao.countByName("JOHN", "DOE");

        assertEquals(1L, count);
    }

    private User createUser(String username, String firstName, String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword("1234567890");
        user.setIsActive(true);

        entityManager.persist(user);
        return user;
    }
}