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
import java.util.List;

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

        Token tokenEntity = new Token();
        tokenEntity.setUser(findUser(username));
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

            Token tokenEntity = findToken(token);

            return !tokenEntity.isExpired() && !tokenEntity.isRevoked();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void revokeUserToken(String token) {
        Token tokenEntity = findToken(token);

        tokenEntity.setExpired(true);
        tokenEntity.setRevoked(true);

        tokenDao.save(tokenEntity);
    }

    @Override
    public void revokeAllUserTokens(String username) {
        List<Token> tokenList = tokenDao.findAllByUserUsernameAndExpiredFalseAndRevokedFalse(username);

        if (tokenList.isEmpty()) {
            return;
        }

        tokenList.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenDao.saveAll(tokenList);
    }

    private User findUser(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    }

    private Token findToken(String token) {
        return tokenDao.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
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
