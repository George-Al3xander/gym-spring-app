package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TokenDao;
import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.model.Token;
import io.github.George_Al3xander.model.TokenType;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Duration expiration;

    private final UserDao userDao;
    private final TokenDao tokenDao;

    @Override
    public Token saveToken(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        Token tokenEntity = new Token();
        tokenEntity.setUser(user);
        tokenEntity.setToken(generateToken(username));
        tokenEntity.setTokenType(TokenType.BEARER);
        tokenEntity.setRevoked(false);
        tokenEntity.setExpired(false);

        return tokenDao.save(tokenEntity);
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token, String usernameFromUserDetails) {
        try {
            String usernameFromToken = extractUsername(token);

            if (!usernameFromToken.equals(usernameFromUserDetails) || isTokenExpired(token)) {
                return false;
            }

            Token tokenEntity = tokenDao.findByToken(token)
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));


            return !tokenEntity.isExpired() && !tokenEntity.isRevoked();
        } catch (Exception e) {
            return false;
        }
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
