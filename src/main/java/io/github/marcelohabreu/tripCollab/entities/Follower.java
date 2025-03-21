package io.github.marcelohabreu.tripCollab.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.compostiteKey.FollowerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table (name = "tb_followers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Follower {
    @EmbeddedId
    private FollowerId id;

    @ManyToOne
    @MapsId("follower_id")
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @MapsId("followed_id")
    @JoinColumn(name = "followed_id")
    private User followed;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "created_at", updatable = false, nullable = false, insertable = false)
    private LocalDateTime createdAt;
}
