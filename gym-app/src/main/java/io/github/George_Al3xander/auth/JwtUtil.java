package io.github.George_Al3xander.auth;

import io.github.common.auth.jwt.JwtAbstractUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtUtil extends JwtAbstractUtil {

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Duration expiration) {
        super(secret, expiration);
    }
}
