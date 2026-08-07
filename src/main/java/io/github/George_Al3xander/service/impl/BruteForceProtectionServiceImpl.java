package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.service.BruteForceProtectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class BruteForceProtectionServiceImpl
        implements BruteForceProtectionService {

    private final int maxAttempts;
    private final long lockTimeMillis;

    private final Map<String, AttemptInfo> attempts =
            new ConcurrentHashMap<>();

    public BruteForceProtectionServiceImpl(
            @Value("${security.brute-force.max-attempts:3}") int maxAttempts,
            @Value("${security.brute-force.lock-time-minutes:5}") long lockTimeMinutes) {
        this.maxAttempts = maxAttempts;
        this.lockTimeMillis =
                TimeUnit.MINUTES.toMillis(lockTimeMinutes);
    }

    @Override
    public void loginSucceeded(String key) {
        attempts.remove(key);
    }

    @Override
    public void loginFailed(String key) {

        attempts.compute(key, (k, info) -> {

            if (info == null) {
                return new AttemptInfo(1, 0);
            }

            int newAttempts = info.attempts + 1;

            if (newAttempts >= maxAttempts) {
                return new AttemptInfo(
                        newAttempts,
                        System.currentTimeMillis()
                );
            }

            return new AttemptInfo(
                    newAttempts,
                    info.lockedAt
            );
        });
    }

    @Override
    public boolean isBlocked(String key) {

        AttemptInfo info = attempts.get(key);

        if (info == null || info.lockedAt == 0) {
            return false;
        }

        if (System.currentTimeMillis() -
                info.lockedAt > lockTimeMillis) {

            attempts.remove(key);
            return false;
        }

        return true;
    }

    private record AttemptInfo(int attempts, long lockedAt) {
    }
}