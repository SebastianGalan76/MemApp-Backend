package com.coresaken.memApp.database.repository.auth;

import com.coresaken.memApp.database.model.auth.ActiveAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveAccountTokenRepository extends JpaRepository<ActiveAccountToken, Long> {
    Optional<ActiveAccountToken> findByUserId(Long userId);

    void deleteByToken(String token);
}
