package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.service.BruteForceProtectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {

    private final int maxAttempts;
    private final long lockTimeMillis;

    public BruteForceProtectionServiceImpl(
            @Value("${security.brute-force.max-attempts:3}") int maxAttempts,
            @Value("${security.brute-force.lock-time-minutes:5}") long lockTimeMinutes) {
        this.maxAttempts = maxAttempts;
        this.lockTimeMillis = TimeUnit.MINUTES.toMillis(lockTimeMinutes);
    }

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockCache = new ConcurrentHashMap<>();

    @Override
    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockCache.remove(key);
    }

    @Override
    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;

        attemptsCache.put(key, attempts);

        if (attempts >= maxAttempts) {
            lockCache.put(key, System.currentTimeMillis());
        }
    }

    @Override
    public boolean isBlocked(String key) {
        if (!lockCache.containsKey(key)) {
            return false;
        }

        long lockTime = lockCache.get(key);
        if (System.currentTimeMillis() - lockTime > lockTimeMillis) {
            lockCache.remove(key);
            return false;
        }

        return true;
    }
}
