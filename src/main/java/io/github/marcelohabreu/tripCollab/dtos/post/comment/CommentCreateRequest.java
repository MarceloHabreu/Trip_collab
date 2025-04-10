package io.github.marcelohabreu.tripCollab.dtos.post.comment;

import jakarta.validation.constraints.Size;

public record CommentCreateRequest(@Size(min = 3, max = 200, message = "the comment must be between 3 and 200 characters") String content) {
}
