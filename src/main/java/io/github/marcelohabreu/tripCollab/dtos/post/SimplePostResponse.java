package io.github.marcelohabreu.tripCollab.dtos.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record SimplePostResponse(UUID postId, String title, String body, String location, String createdBy,
                                 int likeCount, int commentCount, int saveCount,
                                 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt,
                                 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime updatedAt) {
    public static SimplePostResponse fromModel(Post p) {
        return new SimplePostResponse(p.getPostId(), p.getTitle(), p.getBody(), p.getLocation(),
                p.getUser().getUsername(), p.getLikeCount(), p.getCommentCount(),
                p.getSaveCount(), p.getCreatedAt(), p.getUpdatedAt());
    }
}