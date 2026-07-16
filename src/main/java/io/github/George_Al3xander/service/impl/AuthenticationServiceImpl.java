package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.AuthenticationService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDao userDao;

    @Override
    public boolean authenticate(CredentialsDTO credentials) {
        try {
            Optional<User> user = userDao.findByUsername(credentials.getUsername());

            if (user.isEmpty()) {
                return false;
            }

            return user.get().getPassword().equals(credentials.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void changePassword(CredentialsDTO credentials) {
        String username = credentials.getUsername();
        String newPassword = credentials.getPassword();

        try {
            User user = userDao.findByUsername(username).get();

            user.setPassword(newPassword);

            userDao.update(user);
        } catch (NoResultException | NoSuchElementException ex) {
            throw new EntityNotFoundException("User", username);
        }
    }
}
