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
        long count = userDao.countByName(user.getFirstName(), user.getLastName());

        if (count == 0) {
            return baseUsername;
        }
        
        return baseUsername + count;
    }

    private String buildBaseUsername(User user) {
        return user.getFirstName().trim() + "." + user.getLastName().trim();
    }
}
