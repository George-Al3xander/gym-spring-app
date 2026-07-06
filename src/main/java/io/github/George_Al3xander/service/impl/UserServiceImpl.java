package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.UserService;
import io.github.George_Al3xander.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public String resetPassword(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));

        user.setPassword(PasswordGenerator.generatePassword(10));

        userDao.update(user);

        return user.getPassword();
    }

    @Override
    public void toggleActiveStatusByUsername(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", username));

        boolean previousActiveStatus = Boolean.TRUE.equals(user.getIsActive());

        user.setIsActive(!previousActiveStatus);

        userDao.update(user);
    }
}
