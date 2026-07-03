package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
            e.printStackTrace();

            return false;
        }
    }
}
