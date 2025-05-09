package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId")
    List<Comment> findCommentsByUserId(@Param("userId") UUID userId);

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
    List<Comment> findCommentsByPostId(@Param("postId")UUID postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.postId = :postId")
    int countCommentsByPostId (@Param("postId") UUID postId);

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY createdAt")
    Page<Comment> findCommentByPostId(@Param("postId") UUID postId, Pageable pageable);
}
