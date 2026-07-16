package io.github.George_Al3xander.service;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;

public interface AuthenticationService {
    boolean authenticate(CredentialsDTO credentials);

    void changePassword(CredentialsDTO credentials);
}
