package io.github.marcelohabreu.tripCollab.dtos.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostAdminResponse(UUID id, String title,
                                String createdBy,
                                @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                LocalDateTime createdAt,
                                @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                LocalDateTime updatedAt) {
    public static PostAdminResponse fromModel(Post p) {
        return new PostAdminResponse(p.getPostId(), p.getTitle(), p.getUser().getUsername(), p.getCreatedAt(), p.getUpdatedAt());
    }
}
