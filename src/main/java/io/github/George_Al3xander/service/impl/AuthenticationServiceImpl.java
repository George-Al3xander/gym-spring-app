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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(CredentialsDTO credentials) {
        try {
            Optional<User> user = userDao.findByUsername(credentials.getUsername());

            if (user.isEmpty()) {
                return false;
            }

            return passwordEncoder.matches(credentials.getPassword(), user.get().getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void changePassword(ChangeLoginRequest request) {
        String username = request.getUsername();

        try {
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new GymEntityNotFoundException("User", username));

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new BadCredentialsException("Wrong old password");
            }

            user.setPassword(
                    passwordEncoder.encode(request.getNewPassword())
            );

            userDao.update(user);
        } catch (NoResultException | NoSuchElementException ex) {
            throw new GymEntityNotFoundException("User", username);
        }
    }
}
