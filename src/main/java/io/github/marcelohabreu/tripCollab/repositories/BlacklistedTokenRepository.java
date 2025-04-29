package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
    boolean existsByToken(String token);
    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
