package io.github.marcelohabreu.tripCollab.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_blacklisted_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlacklistedToken {
    @Id
    @Column(columnDefinition = "TEXT")
    private String token;
    private LocalDateTime expiresAt;
}