package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenDao extends JpaRepository<Token, Long> {
    List<Token> findAllByUserUsernameAndExpiredFalseAndRevokedFalse(String username);

    Optional<Token> findByToken(String token);

    void deleteAllByExpiredTrueOrRevokedTrue();
}
