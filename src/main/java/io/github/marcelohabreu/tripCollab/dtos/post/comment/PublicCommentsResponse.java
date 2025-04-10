package io.github.marcelohabreu.tripCollab.dtos.post.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

record PublicCommentResponse(UUID commentId, String content, String author, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime commentedAt){
    public static PublicCommentResponse fromModel(Comment c) {
        return new PublicCommentResponse(c.getCommentId(), c.getContent(), c.getUser().getUsername(), c.getCreatedAt());
    }
}

public record PublicCommentsResponse(int countComments, List<PublicCommentResponse> comments) {
    public static PublicCommentsResponse fromModel(List<Comment> c){
        return new PublicCommentsResponse(c.size(), c.stream().map(PublicCommentResponse::fromModel).toList());
    }
}
