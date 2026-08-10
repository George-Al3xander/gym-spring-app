package io.github.George_Al3xander.service;

public interface JwtService {

    String generateToken(String username);

    String extractUsername(String token);

    boolean isTokenValid(String token, String usernameFromUserDetails);

}
