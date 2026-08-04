package io.github.George_Al3xander.auth;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) {

        User user = userDao.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username)
                );

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.getIsActive())
                .roles(user instanceof Trainee ? "TRAINEE" : "TRAINER")
                .build();
    }
}