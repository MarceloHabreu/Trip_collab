package io.github.marcelohabreu.tripCollab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_likes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID like_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User User;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private LocalDateTime liked_at;
}
