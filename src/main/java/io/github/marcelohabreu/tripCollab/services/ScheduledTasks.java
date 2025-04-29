package io.github.marcelohabreu.tripCollab.services;


import io.github.marcelohabreu.tripCollab.repositories.BlacklistedTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduledTasks {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public ScheduledTasks(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Diariamente à meia-noite exclue todos os registros do blacklisted
    // 0 segundos
    // 0 minutos
    // 0 horas
    // * qlqr dia
    // * qlqr semana
    public void cleanExpiredTokens() {
        blacklistedTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}