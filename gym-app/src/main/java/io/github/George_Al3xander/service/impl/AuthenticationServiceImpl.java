package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.exception.GymEntityNotFoundException;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.AuthenticationService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(CredentialsDTO credentials) {
        try {
            User user = findUser(credentials.getUsername());

            validatePassword(credentials.getPassword(), user.getPassword());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void changePassword(ChangeLoginRequest request) {
        String username = request.getUsername();

        try {
            User user = findUser(username);

            validatePassword(request.getOldPassword(), user.getPassword());

            user.setPassword(
                    passwordEncoder.encode(request.getNewPassword())
            );

            userDao.update(user);
        } catch (NoResultException | NoSuchElementException ex) {
            throw new GymEntityNotFoundException("User", username);
        }
    }

    private User findUser(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new GymEntityNotFoundException("User", username));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadCredentialsException("Wrong old password");
        }
    }
}
