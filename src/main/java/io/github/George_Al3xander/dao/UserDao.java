package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.User;

import java.util.Optional;

public interface UserDao extends CrudDao<User> {
    Optional<User> findByUsername(String username);

    long countByName(String firstName, String lastName);
}
