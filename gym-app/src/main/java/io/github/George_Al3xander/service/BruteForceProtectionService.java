package io.github.George_Al3xander.service;

public interface BruteForceProtectionService {
    void loginSucceeded(String key);

    void loginFailed(String key);

    boolean isBlocked(String key);
}
