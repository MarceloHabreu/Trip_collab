package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Like;
import io.github.marcelohabreu.tripCollab.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    // Queries natives SQL:
//    @Query(value = "SELECT COUNT(*) > 0 FROM tb_likes WHERE post_id = :postId AND user_id = :userId", nativeQuery = true)
//    boolean existsByPostId(@Param("postId") UUID postId, @Param("userId") UUID userId);
//
//    @Query(value = "SELECT * FROM tb_likes WHERE post_id = :postId AND user_id = :userId", nativeQuery = true)
//    Like findByPostId(@Param("postId") UUID postId, @Param("userId") UUID userId);

    // Queries with JPQL
    boolean existsByPostPostIdAndUserUserId(UUID postId, UUID userId);

    Optional<Like> findByPostPostIdAndUserUserId(UUID postId, UUID userId);
}
