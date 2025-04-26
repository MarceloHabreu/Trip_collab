package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Like;
import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
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

    @Query("SELECT l.post FROM Like l WHERE l.user.userId = :userId")
    List<Post> findPostsByUserId(@Param("userId") UUID userId);

    @Query("SELECT l.user FROM Like l WHERE l.post.postId = :postId")
    Page<User> findLikersByPostId(@Param("postId") UUID postId, Pageable pageable);
}
