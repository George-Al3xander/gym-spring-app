package io.github.George_Al3xander.service;

public interface UserService {
    String resetPassword(Long id);

    void updateActiveStatusByUsername(String username, boolean active);
}
