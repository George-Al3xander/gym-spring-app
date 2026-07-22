package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.exception.ActivationStateConflictException;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.util.PasswordGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenExistingUser_whenResetPassword_thenReturnsNewPasswordAndPersistsUpdate() {
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        try (MockedStatic<PasswordGenerator> mocked = mockStatic(PasswordGenerator.class)) {
            mocked.when(() -> PasswordGenerator.generatePassword(10))
                    .thenReturn("abcdefghij");

            String result = userService.resetPassword(1L);

            assertEquals("abcdefghij", result);
            assertEquals(10, result.length());

            verify(userDao).update(user);
        }
    }

    @Test
    void givenNonExistingUser_whenResetPassword_thenThrowsEntityNotFoundException() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.resetPassword(1L));

        verify(userDao, never()).update(any());
    }

    @Test
    void givenExistingUser_whenResetPassword_thenPasswordIsChanged() {
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        try (MockedStatic<PasswordGenerator> mocked = mockStatic(PasswordGenerator.class)) {
            mocked.when(() -> PasswordGenerator.generatePassword(10))
                    .thenReturn("abcdefghij");

            String result = userService.resetPassword(1L);

            assertNotEquals("oldPassword", result);
        }
    }

    @Test
    void givenActiveUser_whenUpdateActiveStatusToInactive_thenUserBecomesInactiveAndIsPersisted() {
        User user = new User();
        user.setUsername("john");
        user.setIsActive(true);

        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        userService.updateActiveStatusByUsername("john", false);

        assertFalse(user.getIsActive());
        verify(userDao).update(user);
    }

    @Test
    void givenInactiveUser_whenUpdateActiveStatusToActive_thenUserBecomesActiveAndIsPersisted() {
        User user = new User();
        user.setUsername("john");
        user.setIsActive(false);

        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        userService.updateActiveStatusByUsername("john", true);

        assertTrue(user.getIsActive());
        verify(userDao).update(user);
    }

    @Test
    void givenNullActiveStatusUser_whenUpdateActiveStatusToActive_thenUserBecomesActiveAndIsPersisted() {
        User user = new User();
        user.setUsername("john");
        user.setIsActive(null);

        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        userService.updateActiveStatusByUsername("john", true);

        assertTrue(user.getIsActive());
        verify(userDao).update(user);
    }

    @Test
    void givenNonExistingUser_whenUpdateActiveStatus_thenThrowsEntityNotFoundException() {
        when(userDao.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateActiveStatusByUsername("john", true));

        verify(userDao, never()).update(any());
    }

    @Test
    void givenActiveUser_whenUpdateActiveStatusToActive_thenThrowsActivationStateConflictException() {
        User user = new User();
        user.setUsername("john");
        user.setIsActive(true);

        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        assertThrows(ActivationStateConflictException.class,
                () -> userService.updateActiveStatusByUsername("john", true));

        verify(userDao, never()).update(any());
    }

    @Test
    void givenInactiveUser_whenUpdateActiveStatusToInactive_thenThrowsActivationStateConflictException() {
        User user = new User();
        user.setUsername("john");
        user.setIsActive(false);

        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        assertThrows(ActivationStateConflictException.class,
                () -> userService.updateActiveStatusByUsername("john", false));

        verify(userDao, never()).update(any());
    }
}