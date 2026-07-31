package io.github.George_Al3xander.service;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameGenerator {

    private final UserDao userDao;

    public String generateUsername(User user) {
        String baseUsername = buildBaseUsername(user);

        if (!userDao.existsByUsername(baseUsername)) {
            return baseUsername;
        }

        int suffix = 1;

        while (userDao.existsByUsername(baseUsername + suffix)) {
            suffix++;
        }

        return baseUsername + suffix;
    }

    private String buildBaseUsername(User user) {
        return user.getFirstName().trim() + "." + user.getLastName().trim();
    }
}