package io.github.marcelohabreu.tripCollab.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostAdminView(UUID id, String title,
                            @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                            LocalDateTime createdAt,
                            @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                            LocalDateTime updatedAt) {
    public static PostAdminView fromModel(Post p) {
        return new PostAdminView(p.getPostId(), p.getTitle(), p.getCreatedAt(), p.getUpdatedAt());
    }
}
