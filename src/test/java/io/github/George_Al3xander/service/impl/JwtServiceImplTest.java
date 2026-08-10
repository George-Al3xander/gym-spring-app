package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TokenDao;
import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.model.Token;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private TokenDao tokenDao;

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
        jwtService = new JwtServiceImpl(userDao, tokenDao);

        Field secretField =
                JwtServiceImpl.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtService, SECRET);

        Field expirationField =
                JwtServiceImpl.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, EXPIRATION);
    }

    @Test
    void givenValidUsername_whenSaveToken_thenReturnsValidJwtToken() {

        String username = "john.doe";

        User user = new User();
        user.setUsername(username);

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(tokenDao.save(any(Token.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Token token = jwtService.saveToken(username);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertFalse(token.getToken().isBlank());

        verify(userDao).findByUsername(username);
        verify(tokenDao).save(any(Token.class));
    }

    @Test
    void givenGeneratedToken_whenExtractUsername_thenReturnsOriginalUsername() {

        String username = "john.doe";

        User user = new User();
        user.setUsername(username);

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(tokenDao.save(any(Token.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Token token = jwtService.saveToken(username);

        String extractedUsername =
                jwtService.extractUsername(token.getToken());

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

        User user = new User();
        user.setUsername(username);

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(tokenDao.save(any(Token.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Token token = jwtService.saveToken(username);

        when(tokenDao.findByToken(token.getToken()))
                .thenReturn(Optional.of(token));

        boolean result =
                jwtService.isTokenValid(
                        token.getToken(),
                        username
                );

        assertTrue(result);
    }

    @Test
    void givenDifferentUsernameAndValidToken_whenIsTokenValid_thenReturnsFalse() {

        String username = "john.doe";

        User user = new User();
        user.setUsername(username);

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(tokenDao.save(any(Token.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Token token = jwtService.saveToken(username);

        boolean result =
                jwtService.isTokenValid(
                        token.getToken(),
                        "jane.doe"
                );

        assertFalse(result);

        verify(tokenDao, never()).findByToken(anyString());
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
                        .issuedAt(
                                new Date(
                                        System.currentTimeMillis() - 20000
                                )
                        )
                        .expiration(
                                new Date(
                                        System.currentTimeMillis() - 10000
                                )
                        )
                        .signWith(key)
                        .compact();

        boolean result =
                jwtService.isTokenValid(
                        expiredToken,
                        username
                );

        assertFalse(result);

        verify(tokenDao, never()).findByToken(anyString());
    }

    @Test
    void givenTamperedToken_whenExtractUsername_thenThrowsException() {

        String username = "john.doe";

        User user = new User();
        user.setUsername(username);

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(tokenDao.save(any(Token.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Token token = jwtService.saveToken(username);

        String tamperedToken =
                token.getToken().substring(
                        0,
                        token.getToken().length() - 2
                ) + "ab";

        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername(tamperedToken)
        );
    }

}
