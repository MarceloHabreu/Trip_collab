package io.github.marcelohabreu.tripCollab.dtos.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PublicPostResponse(UUID postId,
                           String title,
                           String body,
                           String location,
                           String createdBy,
                           @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                           LocalDateTime createdAt) {
    public static PublicPostResponse fromModel(Post p) {
        String user = p.getUser().getUsername();
        return new PublicPostResponse(p.getPostId(), p.getTitle(), p.getBody(), p.getLocation(), user, p.getCreatedAt());
    }
}
