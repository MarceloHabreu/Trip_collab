package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Like;
import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.entities.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaveRepository extends JpaRepository<SavedPost, UUID> {


    @Query("SELECT l.post FROM SavedPost l WHERE l.user.userId = :userId")
    List<Post> findPostsByUserId(@Param("userId") UUID userId);

    boolean existsByPostPostIdAndUserUserId(UUID postId, UUID userId);

    Optional<SavedPost> findByPostPostIdAndUserUserId(UUID postId, UUID userId);
}
