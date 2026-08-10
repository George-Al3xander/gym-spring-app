package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Token;

public interface JwtService {

    Token saveToken(String username);

    String extractUsername(String token);

    boolean isTokenValid(String token, String usernameFromUserDetails);

}
