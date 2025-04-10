package io.github.marcelohabreu.tripCollab.dtos.post.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Comment;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Dto to view the post
record CommentPostsResponse(UUID postId, String title, String author){
    public static CommentPostsResponse fromModel(Post p) {
        return new CommentPostsResponse(p.getPostId(), p.getTitle(), p.getUser().getUsername());
    }
}

// Dto to view info comment and your post
record UserCommentResponse(UUID commentId, String content, CommentPostsResponse post,
                                  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime commentedAt) {

    public static UserCommentResponse fromModel(Comment c){
        return new UserCommentResponse(c.getCommentId(),
                c.getContent(),
                CommentPostsResponse.fromModel(c.getPost()),
                c.getCreatedAt());
    }
}

// Dto to view comments in all posts that the user commented
public record UserCommentsResponse(int countComments, List<UserCommentResponse> comments) {
    public static UserCommentsResponse fromModel(List<Comment> c) {
        return new UserCommentsResponse(c.size(), c.stream().map(UserCommentResponse::fromModel).toList());
    }
 }