package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.auth.JwtUtil;
import io.github.George_Al3xander.dao.TokenDao;
import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.exception.GymBadCredentialsException;
import io.github.George_Al3xander.model.Token;
import io.github.George_Al3xander.model.TokenType;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtUtil jwtUtil;

    private final UserDao userDao;
    private final TokenDao tokenDao;

    @Override
    public Token saveToken(String username) {

        Token tokenEntity = new Token();
        tokenEntity.setUser(findUser(username));
        tokenEntity.setToken(jwtUtil.generateToken(username));
        tokenEntity.setTokenType(TokenType.BEARER);
        tokenEntity.setRevoked(false);
        tokenEntity.setExpired(false);

        return tokenDao.save(tokenEntity);
    }

    @Override
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    @Override
    public boolean isTokenValid(String token, String usernameFromUserDetails) {
        try {
            String usernameFromToken = jwtUtil.extractUsername(token);

            if (!usernameFromToken.equals(usernameFromUserDetails) || jwtUtil.isTokenExpired(token)) {
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

    @Scheduled(fixedDelay = 12, timeUnit = TimeUnit.HOURS)
    void cleanTokens() {
        tokenDao.deleteAllByExpiredTrueOrRevokedTrue();
    }

    private User findUser(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new GymBadCredentialsException("Invalid username or password"));
    }

    private Token findToken(String token) {
        return tokenDao.findByToken(token)
                .orElseThrow(() -> new GymBadCredentialsException("Invalid username or password"));
    }
}
