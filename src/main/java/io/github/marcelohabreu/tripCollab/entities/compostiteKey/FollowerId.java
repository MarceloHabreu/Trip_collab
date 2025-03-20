package io.github.marcelohabreu.tripCollab.entities.compostiteKey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowerId implements Serializable {
    private UUID follower_id;
    private UUID followed_id;
}
