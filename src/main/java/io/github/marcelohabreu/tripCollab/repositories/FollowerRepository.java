package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Follower;
import io.github.marcelohabreu.tripCollab.entities.compostiteKey.FollowerId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {

    @Query("SELECT f FROM Follower f WHERE f.followed.userId = :userId ORDER BY f.followed.username")
    List<Follower> findByFollowed_UserId(UUID userId, Pageable pageable);

    @Query("SELECT f FROM Follower f WHERE f.follower.userId = :userId ORDER BY f.follower.username")
    List<Follower> findByFollower_UserId(UUID userId, Pageable pageable);
}
