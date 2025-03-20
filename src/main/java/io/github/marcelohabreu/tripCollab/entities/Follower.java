package io.github.marcelohabreu.tripCollab.entities;

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

    private LocalDateTime created_at;
}
