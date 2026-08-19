package io.github.George_Al3xander.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BruteForceProtectionServiceImplTest {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_MINUTES = 5;

    private BruteForceProtectionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BruteForceProtectionServiceImpl(
                MAX_ATTEMPTS,
                LOCK_TIME_MINUTES
        );
    }

    @Test
    void givenNewKey_whenIsBlocked_thenReturnsFalse() {
        assertFalse(service.isBlocked("user"));
    }

    @Test
    void givenOneFailedLogin_whenIsBlocked_thenReturnsFalse() {
        service.loginFailed("user");

        assertFalse(service.isBlocked("user"));
    }

    @Test
    void givenTwoFailedLogins_whenIsBlocked_thenReturnsFalse() {
        service.loginFailed("user");
        service.loginFailed("user");

        assertFalse(service.isBlocked("user"));
    }

    @Test
    void givenMaximumFailedLogins_whenIsBlocked_thenReturnsTrue() {
        service.loginFailed("user");
        service.loginFailed("user");
        service.loginFailed("user");

        assertTrue(service.isBlocked("user"));
    }

    @Test
    void givenBlockedUser_whenLoginFailedAgain_thenRemainsBlocked() {
        service.loginFailed("user");
        service.loginFailed("user");
        service.loginFailed("user");

        service.loginFailed("user");

        assertTrue(service.isBlocked("user"));
    }

    @Test
    void givenBlockedUser_whenLoginSucceeded_thenUserIsUnblocked() {
        service.loginFailed("user");
        service.loginFailed("user");
        service.loginFailed("user");

        assertTrue(service.isBlocked("user"));

        service.loginSucceeded("user");

        assertFalse(service.isBlocked("user"));
    }

    @Test
    void givenFailedAttempts_whenLoginSucceeded_thenAttemptsAreReset() {
        service.loginFailed("user");
        service.loginFailed("user");

        service.loginSucceeded("user");

        service.loginFailed("user");
        service.loginFailed("user");

        assertFalse(service.isBlocked("user"));

        service.loginFailed("user");

        assertTrue(service.isBlocked("user"));
    }

    @Test
    void givenUnknownUser_whenLoginSucceeded_thenUserRemainsUnblocked() {
        assertDoesNotThrow(() -> service.loginSucceeded("unknown"));

        assertFalse(service.isBlocked("unknown"));
    }

    @Test
    void givenMultipleUsers_whenOneUserFails_thenOnlyFailedUserIsBlocked() {
        service.loginFailed("user1");
        service.loginFailed("user1");
        service.loginFailed("user1");

        service.loginFailed("user2");

        assertTrue(service.isBlocked("user1"));
        assertFalse(service.isBlocked("user2"));
    }

    @Test
    void givenCustomMaximumAttempts_whenThresholdReached_thenUserIsBlocked() {
        BruteForceProtectionServiceImpl customService =
                new BruteForceProtectionServiceImpl(5, LOCK_TIME_MINUTES);

        customService.loginFailed("user");
        customService.loginFailed("user");
        customService.loginFailed("user");
        customService.loginFailed("user");

        assertFalse(customService.isBlocked("user"));

        customService.loginFailed("user");

        assertTrue(customService.isBlocked("user"));
    }

    @Test
    void givenDifferentKeys_whenFailedAttemptsAreRecorded_thenEachKeyHasIndependentAttempts() {
        service.loginFailed("user1");
        service.loginFailed("user1");

        service.loginFailed("user2");
        service.loginFailed("user2");
        service.loginFailed("user2");

        assertFalse(service.isBlocked("user1"));
        assertTrue(service.isBlocked("user2"));
    }

    @Test
    void givenSuccessfulLoginAfterFailedAttempts_whenFailedAgain_thenAttemptsStartFromZero() {
        service.loginFailed("user");
        service.loginFailed("user");

        service.loginSucceeded("user");

        service.loginFailed("user");
        service.loginFailed("user");

        assertFalse(service.isBlocked("user"));
    }
}