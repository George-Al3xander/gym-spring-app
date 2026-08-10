package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtService jwtService;

    private static final String SECRET =
            Base64.getEncoder()
                    .encodeToString(
                            "my-super-secret-key-my-super-secret-key".getBytes()
                    );

    private static final Duration EXPIRATION =
            Duration.ofMinutes(10);


    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtServiceImpl();

        Field secretField = JwtServiceImpl.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtService, SECRET);

        Field expirationField = JwtServiceImpl.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, EXPIRATION);
    }


    @Test
    void givenValidUsername_whenGenerateToken_thenReturnsValidJwtToken() {

        String username = "john.doe";

        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }


    @Test
    void givenGeneratedToken_whenExtractUsername_thenReturnsOriginalUsername() {

        String username = "john.doe";

        String token = jwtService.generateToken(username);

        String extractedUsername =
                jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }


    @Test
    void givenMalformedToken_whenExtractUsername_thenThrowsException() {

        String invalidToken = "invalid.jwt.token";

        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername(invalidToken)
        );
    }


    @Test
    void givenMatchingUsernameAndValidToken_whenIsTokenValid_thenReturnsTrue() {

        String username = "john.doe";

        String token = jwtService.generateToken(username);

        boolean result =
                jwtService.isTokenValid(token, username);

        assertTrue(result);
    }


    @Test
    void givenDifferentUsernameAndValidToken_whenIsTokenValid_thenReturnsFalse() {

        String token =
                jwtService.generateToken("john.doe");


        boolean result =
                jwtService.isTokenValid(
                        token,
                        "jane.doe"
                );

        assertFalse(result);
    }


    @Test
    void givenExpiredToken_whenIsTokenValid_thenReturnsFalse() {

        String username = "john.doe";

        SecretKey key =
                Keys.hmacShaKeyFor(
                        Base64.getDecoder()
                                .decode(SECRET)
                );


        String expiredToken =
                Jwts.builder()
                        .subject(username)
                        .issuedAt(new Date(System.currentTimeMillis() - 20000))
                        .expiration(new Date(System.currentTimeMillis() - 10000))
                        .signWith(key)
                        .compact();


        boolean result =
                jwtService.isTokenValid(
                        expiredToken,
                        username
                );

        assertFalse(result);
    }


    @Test
    void givenTamperedToken_whenExtractUsername_thenThrowsException() {

        String token =
                jwtService.generateToken("john.doe");


        String tamperedToken =
                token.substring(0, token.length() - 2) + "ab";


        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername(tamperedToken)
        );
    }
}